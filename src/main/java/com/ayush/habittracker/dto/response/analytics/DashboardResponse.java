package com.ayush.habittracker.dto.response.analytics;

import java.util.List;

import com.ayush.habittracker.dto.response.HabitResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private int totalHabits;
    private int activeHabits;
    private List<HabitResponse> allHabits;
    private double overallSuccessRate;
    private HabitResponse bestHabit;
    private HabitResponse worstHabit;
}

