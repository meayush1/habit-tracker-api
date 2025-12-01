package com.ayush.habittracker.dto.request;

import com.ayush.habittracker.model.Category;
import com.ayush.habittracker.model.HabitFrequency;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Title can't be empty")
    @Size(min=2,max=30)
    private String title;

    @Size(max=255)
    private String description;

    @NotNull
    private Category category;

    @NotNull
    private HabitFrequency frequency;

    private boolean isActive = true;
}

