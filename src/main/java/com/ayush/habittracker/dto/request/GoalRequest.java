package com.ayush.habittracker.dto.request;

import java.time.LocalDate;

import com.ayush.habittracker.model.Category;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalRequest {
	
	@NotNull(message="User Id can't be null")
	private Long userId;
	
	@Size(min=3,max=30,message="Goal title must be between 3 & 30 character long")
    private String title;
	
	@Size(max=256,message="Description cannot be more than 256 character long")
    private String description;
    
    @Future(message="Target date must be in future")
    @NotNull(message="Target date can't be empty")
    private LocalDate targetDate;

    @Min(value=0,message="Currenet progress cannot be negative")
    private int currentProgress;
    @NotNull(message="Target value can't be empty")
    
    @Min(value=1,message="Target value cannot be less than 1")
    private int targetValue;

    @Enumerated(EnumType.STRING)
    @NotNull()
    private Category category;
}
