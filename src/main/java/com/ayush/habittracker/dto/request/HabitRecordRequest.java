package com.ayush.habittracker.dto.request;

import java.time.LocalDate;

import com.ayush.habittracker.model.HabitStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitRecordRequest {
	@NotNull(message="Habit id is required")
	private Long habitId;

    @NotNull(message="Date can't be null")
    @PastOrPresent(message = "Record date cannot be in the future")
    private LocalDate date;       // day-specific record
    
    @NotNull(message="status is required")
    @Enumerated(EnumType.STRING)
    private HabitStatus status;        // DONE, MISSED
    
    @Size(max=256,message="Description cannot be more than 256 character long")
    private String note;          // optional
}
