package com.ayush.habittracker.analytics;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.habittracker.common.ApiResponse;
import com.ayush.habittracker.common.ResponseUtil;
import com.ayush.habittracker.dto.response.analytics.DashboardResponse;
import com.ayush.habittracker.dto.response.analytics.MonthlySummaryResponse;
import com.ayush.habittracker.dto.response.analytics.WeeklySummaryResponse;

@RestController
@RequestMapping("/analytics")
public class HabitAnalyticsController {

    @Autowired
    private HabitAnalyticsService analyticsService;

    // ---------------------------- STREAKS ----------------------------

    @GetMapping("/habit/{habitId}/streak/current")
    public ResponseEntity<ApiResponse<Integer>> getCurrentStreak(@PathVariable Long habitId) {

        int streak = analyticsService.getCurrentStreak(habitId);

        return ResponseUtil.success(
                streak,
                "Current streak calculated",
                HttpStatus.OK,
                Map.of("habitId", habitId)
        );
    }

    @GetMapping("/habit/{habitId}/streak/longest")
    public ResponseEntity<ApiResponse<Integer>> getLongestStreak(@PathVariable Long habitId) {

        int longestStreak = analyticsService.getLongestStreak(habitId);

        return ResponseUtil.success(
                longestStreak,
                "Longest streak calculated",
                HttpStatus.OK,
                Map.of("habitId", habitId)
        );
    }

    // ---------------------------- COMPLETION RATE ----------------------------

    @GetMapping("/habit/{habitId}/completion-rate")
    public ResponseEntity<ApiResponse<Double>> getCompletionRate(@PathVariable Long habitId) {

        double rate = analyticsService.getCompletionRate(habitId);

        return ResponseUtil.success(
                rate,
                "Completion rate calculated",
                HttpStatus.OK,
                Map.of("habitId", habitId)
        );
    }

    // ---------------------------- WEEKLY SUMMARY ----------------------------

    @GetMapping("/habit/{habitId}/weekly")
    public ResponseEntity<ApiResponse<WeeklySummaryResponse>> getWeeklySummary(@PathVariable Long habitId) {

        WeeklySummaryResponse summary = analyticsService.getWeeklySummary(habitId);

        return ResponseUtil.success(
                summary,
                "Weekly summary generated",
                HttpStatus.OK,
                Map.of("habitId", habitId)
        );
    }

    // ---------------------------- MONTHLY SUMMARY ----------------------------

    @GetMapping("/habit/{habitId}/monthly")
    public ResponseEntity<ApiResponse<MonthlySummaryResponse>> getMonthlySummary(@PathVariable Long habitId) {

        MonthlySummaryResponse summary = analyticsService.getMonthlySummary(habitId);

        return ResponseUtil.success(
                summary,
                "Monthly summary generated",
                HttpStatus.OK,
                Map.of("habitId", habitId)
        );
    }

    // ---------------------------- USER DASHBOARD ----------------------------

    @GetMapping("/user/{userId}/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getUserDashboard(@PathVariable Long userId) {

        DashboardResponse response = analyticsService.getUserDashboard(userId);

        return ResponseUtil.success(
                response,
                "User dashboard analytics generated",
                HttpStatus.OK,
                Map.of("userId", userId)
        );
    }
}

