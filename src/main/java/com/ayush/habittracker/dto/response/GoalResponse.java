package com.ayush.habittracker.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponse {
	private long id;
	private long userId;
	private String title;
	private String description;
	private LocalDate targetDate;
	private int currentProgress;
	private int targetValue;
	private double progressPercentage;
}
