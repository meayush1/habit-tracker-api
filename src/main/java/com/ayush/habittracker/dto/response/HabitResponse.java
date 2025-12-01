package com.ayush.habittracker.dto.response;

import java.time.LocalDateTime;

import com.ayush.habittracker.model.Category;
import com.ayush.habittracker.model.HabitFrequency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitResponse {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Category category;
    private HabitFrequency frequency;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

