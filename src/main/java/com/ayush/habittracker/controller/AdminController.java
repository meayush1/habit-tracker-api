package com.ayush.habittracker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.habittracker.common.ApiResponse;
import com.ayush.habittracker.common.ResponseUtil;
import com.ayush.habittracker.dto.response.UserResponse;
import com.ayush.habittracker.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    // ADMIN: View all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers() {
        return ResponseUtil.success(userService.getAllUsers(0, 100, "id"),"Users fatched successfully",HttpStatus.OK,Map.of("endpoint","/users"));
    }

    // ADMIN: Delete any user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
    	return ResponseUtil.success(userService.deleteUser(id),"User deleted by admin successfully",HttpStatus.OK,Map.of("endpoint","/users/id"));
    }
}
