package com.ayush.habittracker.dto.response.analytics;

import java.time.LocalDate;
import java.util.Map;

import com.ayush.habittracker.model.HabitStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummaryResponse {
    private int totalDays;
    private int completedDays;
    private int missedDays;
    private double completionRate;
    private Map<LocalDate, HabitStatus> dailyBreakdown; // 1–31
}

