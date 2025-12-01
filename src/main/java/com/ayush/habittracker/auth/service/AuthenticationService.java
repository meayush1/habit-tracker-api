package com.ayush.habittracker.auth.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ayush.habittracker.auth.dto.LoginRequest;
import com.ayush.habittracker.auth.dto.LoginResponse;
import com.ayush.habittracker.dto.request.UserRequest;
import com.ayush.habittracker.dto.response.UserResponse;
import com.ayush.habittracker.exception.UserNotFoundException;
import com.ayush.habittracker.model.User;
import com.ayush.habittracker.repository.UserRepository;
import com.ayush.habittracker.security.jwt.JwtUtil;
import com.ayush.habittracker.service.UserService;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ModelMapper mapper;

    /** REGISTER USER **/
    public UserResponse register(UserRequest req) {
        return userService.createUser(req);
    }

    /** LOGIN **/
    public LoginResponse login(LoginRequest req) {

        User user = userRepo.findByEmail(req.getEmail());
        if (user == null) {
            throw new UserNotFoundException("Invalid email or password");
        }

        // Check password
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user);

        UserResponse userRes = mapper.map(user, UserResponse.class);

        return new LoginResponse(token, userRes);
    }

    /** GET LOGGED-IN USER **/
    public UserResponse getMe(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) throw new UserNotFoundException("User not found");

        return mapper.map(user, UserResponse.class);
    }
}
