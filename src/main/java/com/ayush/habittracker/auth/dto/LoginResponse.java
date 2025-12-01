package com.ayush.habittracker.auth.dto;

import com.ayush.habittracker.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserResponse user;
}
