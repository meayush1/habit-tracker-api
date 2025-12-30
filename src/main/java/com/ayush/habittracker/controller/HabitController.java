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
import com.ayush.habittracker.dto.request.HabitRequest;
import com.ayush.habittracker.dto.response.HabitResponse;
import com.ayush.habittracker.model.Category;
import com.ayush.habittracker.service.HabitService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    // Create a habit
    @PostMapping
    public ResponseEntity<ApiResponse<HabitResponse>> createHabit(@Valid @RequestBody HabitRequest req) {
        HabitResponse res = habitService.createHabit(req.getUserId(), req);
        return ResponseUtil.success(res, "New habit created", HttpStatus.CREATED, Map.of("endpoint", "/habits"));
    }

    // Get habit by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HabitResponse>> getHabit(@PathVariable Long id) {
        HabitResponse res = habitService.getHabitById(id);
        return ResponseUtil.success(res, "Habit fetched", HttpStatus.OK, Map.of("id", id));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Page<HabitResponse>>> getHabitsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        var pageRes = habitService.getAllHabitsByUser(page, size, sortBy, direction, userId);
        return ResponseUtil.success(pageRes, "Habits fetched", HttpStatus.OK,
                Map.of("page", page, "size", size, "sortBy", sortBy, "direction", direction, "userId", userId));
    }
    // Get all habits with pagination & sorting
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<HabitResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        var pageRes = habitService.getAllHabits(page, size, sortBy, direction);
        return ResponseUtil.success(pageRes, "Habits fetched", HttpStatus.OK,
                Map.of("page", page, "size", size, "sortBy", sortBy, "direction", direction));
    }

    // Search habits by title keyword
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HabitResponse>>> search(@RequestParam String keyword) {
        var list = habitService.searchHabits(keyword);
        return ResponseUtil.success(list, "Search results", HttpStatus.OK, Map.of("keyword", keyword));
    }

    // Filter by category (Category is an enum; Spring will convert from string)
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<HabitResponse>>> filterByCategory(@RequestParam Category category) {
        var list = habitService.filterByCategory(category);
        return ResponseUtil.success(list, "Filtered by category", HttpStatus.OK, Map.of("category", category));
    }

    // Update habit
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HabitResponse>> updateHabit(@PathVariable Long id,
            @Valid @RequestBody HabitRequest req) {
        HabitResponse res = habitService.updateHabit(id, req);
        return ResponseUtil.success(res, "Habit updated", HttpStatus.OK, Map.of("id", id));
    }

    // Delete habit
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<HabitResponse>> deleteHabit(@PathVariable Long id) {
        HabitResponse res=habitService.deleteHabit(id);
        return ResponseUtil.success(res, "Habit deleted", HttpStatus.OK, Map.of("id", id));
    }
}
