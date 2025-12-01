package com.ayush.habittracker.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ayush.habittracker.auth.dto.LoginRequest;
import com.ayush.habittracker.auth.dto.LoginResponse;
import com.ayush.habittracker.auth.service.AuthenticationService;
import com.ayush.habittracker.common.ApiResponse;
import com.ayush.habittracker.common.ResponseUtil;
import com.ayush.habittracker.dto.request.UserRequest;
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
        return ResponseUtil.success(user, "User registered", HttpStatus.CREATED, null);
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
}
