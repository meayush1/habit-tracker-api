package com.ayush.habittracker.controller;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ayush.habittracker.common.ApiResponse;
import com.ayush.habittracker.common.ResponseUtil;
import com.ayush.habittracker.dto.request.GoalRequest;
import com.ayush.habittracker.dto.response.GoalResponse;
import com.ayush.habittracker.model.Category;
import com.ayush.habittracker.service.GoalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    // Create a new goal
    @PostMapping
    public ResponseEntity<ApiResponse<GoalResponse>> createGoal(@Valid @RequestBody GoalRequest req) {
        GoalResponse res = goalService.createGoal(req);
        return ResponseUtil.success(res, "Goal created", HttpStatus.CREATED, Map.of("endpoint", "/goals"));
    }

    // Get goal by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GoalResponse>> getGoalById(@PathVariable Long id) {
        GoalResponse res = goalService.getGoalById(id);
        return ResponseUtil.success(res, "Goal fetched", HttpStatus.OK, Map.of("id", id));
    }

    // Get all goals for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<GoalResponse>>> getGoalsByUser(@PathVariable Long userId) {
        List<GoalResponse> res = goalService.getGoalsByUser(userId);
        return ResponseUtil.success(res, "Goals for user fetched", HttpStatus.OK, Map.of("userId", userId));
    }

    // Get goals by category (Category enum will be mapped automatically from string)
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<GoalResponse>>> getGoalsByCategory(@PathVariable Category category) {
        List<GoalResponse> res = goalService.getGoalsByCategory(category);
        return ResponseUtil.success(res, "Goals by category fetched", HttpStatus.OK, Map.of("category", category));
    }

    // Update goal
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GoalResponse>> updateGoal(@PathVariable Long id,
                                                                @Valid @RequestBody GoalRequest req) {
        GoalResponse res = goalService.updateGoal(id, req);
        return ResponseUtil.success(res, "Goal updated", HttpStatus.OK, Map.of("id", id));
    }

    // Delete goal
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGoal(@PathVariable Long id) {
        String msg = goalService.deleteGoal(id);
        return ResponseUtil.success(msg, "Goal deleted", HttpStatus.OK, Map.of("id", id));
    }

    // Get progress percentage
    @GetMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<Double>> getProgress(@PathVariable Long id) {
        double pct = goalService.getProgressPercentage(id);
        return ResponseUtil.success(pct, "Progress percentage calculated", HttpStatus.OK, Map.of("id", id));
    }
}
