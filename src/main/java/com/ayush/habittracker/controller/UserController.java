package com.ayush.habittracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.habittracker.common.ApiResponse;
import com.ayush.habittracker.common.ResponseUtil;
import com.ayush.habittracker.dto.request.UserRequest;
import com.ayush.habittracker.dto.response.UserResponse;
import com.ayush.habittracker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping
	public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserRequest req) {
		return ResponseUtil.success(service.createUser(req), "User created successfully", HttpStatus.CREATED,
				Map.of("endpoint", "/users"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
		return ResponseUtil.success(service.getUserById(id), "User fetched", HttpStatus.OK, Map.of("id", id));
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<Page<UserResponse>>> getAll(@RequestParam int page, @RequestParam int size,
			@RequestParam(defaultValue = "id") String sortBy) {
		return ResponseUtil.success(service.getAllUsers(page, size, sortBy), "Users fetched", HttpStatus.OK, Map.of());
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<UserResponse>>> search(@RequestParam String keyword) {
		return ResponseUtil.success(service.searchUser(keyword), "Search results", HttpStatus.OK,
				Map.of("keyword", keyword));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable Long id,
			@Valid @RequestBody UserRequest req) {
		return ResponseUtil.success(service.updateUser(id, req), "User updated", HttpStatus.OK, Map.of("id", id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {

		return ResponseUtil.success(service.deleteUser(id), "Success", HttpStatus.OK, Map.of("id", id));
	}
}
