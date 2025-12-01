package com.ayush.habittracker.dto.response;

import java.time.LocalDate;

import com.ayush.habittracker.model.HabitStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitRecordResponse {
	private Long id;
	private Long habitId;
	private HabitStatus status;
	private String note;
	private LocalDate date;
}
