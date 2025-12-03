package com.ayush.habittracker.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.habittracker.auth.dto.LoginRequest;
import com.ayush.habittracker.auth.dto.LoginResponse;
import com.ayush.habittracker.auth.service.AuthenticationService;
import com.ayush.habittracker.common.ApiResponse;
import com.ayush.habittracker.common.ResponseUtil;
import com.ayush.habittracker.dto.request.ChangePasswordRequest;
import com.ayush.habittracker.dto.request.EmailRequest;
import com.ayush.habittracker.dto.request.UserRequest;
import com.ayush.habittracker.dto.request.VerifyOtpRequest;
import com.ayush.habittracker.dto.response.UserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRequest req) {
		UserResponse user = authService.register(req);
		return ResponseUtil.success(user, "User registered and a verification code sended to your email",
				HttpStatus.CREATED, null);
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req) {
		LoginResponse res = authService.login(req);
		return ResponseUtil.success(res, "Login successful", HttpStatus.OK, null);
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserResponse>> me(@RequestAttribute("email") String email) {
		UserResponse user = authService.getMe(email);
		return ResponseUtil.success(user, "User data fetched", HttpStatus.OK, null);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody VerifyOtpRequest req) {
		String msg = authService.verifyOtp(req);
		return ResponseUtil.success(msg, "OTP verified", HttpStatus.OK, null);
	}

	@PostMapping("/resend-otp")
	public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody EmailRequest req) {

		String msg = authService.resendOtp(req);
		return ResponseUtil.success(msg, "OTP resent", HttpStatus.OK, null);
	}

	@PostMapping("/password-reset/send-otp")
	public ResponseEntity<ApiResponse<String>> sendOtpforPasswordReset(@Valid @RequestBody EmailRequest req) {
		String msg = authService.sendOtpForChangePassword(req);
		return ResponseUtil.success(msg, "OTP send", HttpStatus.OK, null);
	}

	@PostMapping("/password-reset/verify-otp")
	public ResponseEntity<ApiResponse<String>> varifyOtpforPasswordReset(@Valid @RequestBody VerifyOtpRequest req) {
		String msg = authService.verifyOtpOfForgetPassword(req);
		return ResponseUtil.success(msg, "OTP verified", HttpStatus.OK, null);
	}

	@PostMapping("/password-reset/reset-password")
	public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ChangePasswordRequest req) {
		String msg = authService.chnagePassword(req);
		return ResponseUtil.success(msg, "Password changed", HttpStatus.OK, null);
	}
}
