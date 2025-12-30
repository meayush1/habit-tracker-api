package com.ayush.habittracker.auth.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ayush.habittracker.auth.dto.LoginRequest;
import com.ayush.habittracker.auth.dto.LoginResponse;
import com.ayush.habittracker.dto.request.ChangePasswordRequest;
import com.ayush.habittracker.dto.request.EmailRequest;
import com.ayush.habittracker.dto.request.UserRequest;
import com.ayush.habittracker.dto.request.VerifyOtpRequest;
import com.ayush.habittracker.dto.response.UserResponse;
import com.ayush.habittracker.exception.EmailAlreadyExistsException;
import com.ayush.habittracker.exception.UserNotFoundException;
import com.ayush.habittracker.model.Otp;
import com.ayush.habittracker.model.OtpType;
import com.ayush.habittracker.model.Role;
import com.ayush.habittracker.model.User;
import com.ayush.habittracker.repository.OtpRepository;
import com.ayush.habittracker.repository.UserRepository;
import com.ayush.habittracker.security.jwt.JwtUtil;
import com.ayush.habittracker.security.util.AuthUtil;
import com.ayush.habittracker.service.EmailService;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	OtpRepository otpRepo;
	@Autowired
	EmailService emailService;

	@Autowired
	AuthUtil authUtil;

	/** REGISTER USER **/
	public UserResponse register(UserRequest req) {
		// check if the email is already exists or not
		if (userRepo.existsByEmail(req.getEmail())) {
			throw new EmailAlreadyExistsException("Email already registered: " + req.getEmail());
		}

		// change userRequest->user
		User user = mapper.map(req, User.class);

		// set time stamp and save user in DB
		user.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		// user.setRole(Role.ADMIN);
		user.setVerified(false);

		// OTP logic create the otp and send it to the email
		createAndSendOtp(req.getEmail(), OtpType.REGISTRATION);

		User savedUser = userRepo.save(user);
		// again change user -> userResponse
		UserResponse userRes = mapper.map(savedUser, UserResponse.class);
		// return userReponse
		return userRes;
	}

	/** LOGIN **/
	public LoginResponse login(LoginRequest req) {

		User user = userRepo.findByEmail(req.getEmail());
		if (user == null) {
			throw new UserNotFoundException("Invalid email or password");
		}
		if (!user.isVerified()) {
			throw new RuntimeException("Please verify your email first");
		}

		// Check password
		if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
			throw new UserNotFoundException("Invalid email or password");
		}

		// Generate JWT token
		String token = jwtUtil.generateToken(user);
		
		UserResponse userRes = mapper.map(user, UserResponse.class);

		return new LoginResponse(token,userRes);
	}

	/** GET LOGGED-IN USER **/
	public UserResponse getMe(String email) {
		User user = userRepo.findByEmail(email);
		if (user == null)
			throw new UserNotFoundException("User not found");

		return mapper.map(user, UserResponse.class);
	}

	public void createAndSendOtp(String email, OtpType type) {
		String otp = String.valueOf(new Random().nextInt(900000) + 100000);
		// delete the existing record from this repo
		Otp existing = otpRepo.findByEmail(email);
		System.out.println(existing);

		Otp emailOtp = new Otp();
		// delete existing otps
		if (existing != null) {
			emailOtp.setAttempts(existing.getAttempts() + 1);
			otpRepo.delete(existing);
		}
		emailOtp.setEmail(email);
		emailOtp.setOtp(otp);
		emailOtp.setExpiredAt(LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(5));
		emailOtp.setLastSentAt(LocalDateTime.now(ZoneId.of("UTC")));
		emailOtp.setOtpType(type);
		otpRepo.save(emailOtp);

		// send email
		emailService.sendOtp(email, otp);

	}

	@Transactional
	public String resendOtp(EmailRequest req) {

		Otp savedOtp = otpRepo.findByEmail(req.getEmail());
		if (savedOtp == null) {
			throw new RuntimeException("No OTP found. Please register again.");
		}

		// 1-minute cooldown
		if (savedOtp.getLastSentAt().isAfter(LocalDateTime.now(ZoneId.of("UTC")).minusMinutes(1))) {
			throw new RuntimeException("OTP already sent. Please wait 1 minute for resend it again.");
		}

		// Limit 5 OTP attempts per day
		if (savedOtp.getAttempts() >= 5
				&& savedOtp.getLastSentAt().isAfter(LocalDateTime.now(ZoneId.of("UTC")).minusDays(1))) {

			throw new RuntimeException("Maximum OTP attempts exceeded. Try again tomorrow.");
		}

		savedOtp.setExpiredAt(LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(5));
		savedOtp.setLastSentAt(LocalDateTime.now(ZoneId.of("UTC")));
		savedOtp.setAttempts(savedOtp.getAttempts() + 1);

		otpRepo.save(savedOtp);

		// Send OTP email
		emailService.sendOtp(req.getEmail(), savedOtp.getOtp());

		return "OTP resent successfully";
	}

	@Transactional
	public String verifyOtp(VerifyOtpRequest req) {

		Otp savedOtp = otpRepo.findByEmail(req.getEmail());
		if (savedOtp == null) {
			throw new RuntimeException("OTP not found or expired");
		}
		if (savedOtp.getOtpType() != OtpType.REGISTRATION)
			throw new RuntimeException("Invalid OTP type");

		if (!savedOtp.getOtp().equals(req.getOtp())) {
			throw new RuntimeException("Invalid OTP");
		}
		if (savedOtp.getExpiredAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")))) {
			otpRepo.deleteByEmail(req.getEmail());
			throw new RuntimeException("OTP expired");
		}

		User user = userRepo.findByEmail(req.getEmail());
		user.setVerified(true);
		user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		userRepo.save(user);

		otpRepo.deleteByEmail(req.getEmail());

		return "Email verified successfully";
	}

	@Transactional
	public String verifyOtpOfForgetPassword(VerifyOtpRequest req) {

		Otp savedOtp = otpRepo.findByEmail(req.getEmail());
		if (savedOtp == null) {
			throw new RuntimeException("OTP not found or expired for mailid: " + req.getEmail());
		}
		if (savedOtp.getOtpType() != OtpType.FORGOT_PASSWORD) {
			throw new RuntimeException("This OTP cannot be used for password reset");
		}
		if (!savedOtp.getOtp().equals(req.getOtp())) {
			throw new RuntimeException("Invalid OTP");
		}

		if (savedOtp.getExpiredAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")))) {
			otpRepo.deleteByEmail(req.getEmail());
			throw new RuntimeException("OTP expired");
		}
		savedOtp.setVerifiedForPasswordReset(true);
		return "OTP verified — You can now reset your password.";

	}

	public String sendOtpForChangePassword(EmailRequest req) {

		createAndSendOtp(req.getEmail(), OtpType.FORGOT_PASSWORD);
		return "OTP sended successfully to mail id: " + req.getEmail();

	}

	@Transactional
	public String chnagePassword(ChangePasswordRequest req) {
		Otp otp = otpRepo.findByEmail(req.getEmail());

		if (otp == null || !otp.isVerifiedForPasswordReset())
			throw new RuntimeException("Password reset not authorized");

		if (!req.getNewPassword().equals(req.getVerifyPassword())) {
			throw new RuntimeException("Passwords do not match");
		}
		User user = userRepo.findByEmail(req.getEmail());
		if (user == null)
			throw new UserNotFoundException("User not found with email id : " + req.getEmail());
		user.setPassword(passwordEncoder.encode(req.getNewPassword()));
		user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		userRepo.save(user);
		// once password changed
		otpRepo.deleteByEmail(req.getEmail());
		return "Password changed successfully";
	}

}
