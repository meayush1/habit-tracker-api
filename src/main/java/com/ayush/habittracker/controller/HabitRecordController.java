package com.ayush.habittracker.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.habittracker.common.ApiResponse;
import com.ayush.habittracker.common.ResponseUtil;
import com.ayush.habittracker.dto.request.HabitRecordRequest;
import com.ayush.habittracker.dto.response.HabitRecordResponse;
import com.ayush.habittracker.service.HabitRecordService;

import jakarta.validation.Valid;

@RequestMapping("/habit_records")
@RestController
public class HabitRecordController {
	@Autowired
	HabitRecordService habitRecordService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<HabitRecordResponse>> createHabitRecord(@Valid @RequestBody HabitRecordRequest body){
		HabitRecordResponse habitRecord=habitRecordService.createHabitRecord(body);
		return ResponseUtil.success(habitRecord, "Habit record created", HttpStatus.CREATED, Map.of("endpoint","/habit_records"));
	}
	@GetMapping("/habit/{habitId}")
	public ResponseEntity<ApiResponse<List<HabitRecordResponse>>> getHabitRecordsByHabitId(@PathVariable Long habitId){
		List<HabitRecordResponse> habitRecords=habitRecordService.findAllHabitRecordsOfHabit(habitId);
		String res="All habit records fatched for habitId: "+habitId;
		return ResponseUtil.success(habitRecords, res, HttpStatus.OK, Map.of("HabitId",habitId));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<HabitRecordResponse>>> getAllHabitsByDate(@RequestParam LocalDate date){
		List<HabitRecordResponse> habitRecords=habitRecordService.findAllHabitRecordsByDate(date);
		String res="All habit records fatched for date: "+ date;
		return ResponseUtil.success(habitRecords, res, HttpStatus.OK, Map.of("date",date));
	}
	
	@GetMapping("/habit")
	public ResponseEntity<ApiResponse<List<HabitRecordResponse>>> getAllHabitsByHabitIdAndDateBtween(@RequestParam Long habitId,@RequestParam LocalDate startDate,@RequestParam LocalDate endDate){
		List<HabitRecordResponse> habitRecords=habitRecordService.findAllHabitRecordsByHabitIdAndDateBetween(habitId, startDate, endDate);
		String res="All habit records fatched for habitId: "+ habitId + "And date from "+startDate+" to "+endDate;
		return ResponseUtil.success(habitRecords, res, HttpStatus.OK, Map.of("habitId",habitId));
	}
	
	@PutMapping("/{habitRecordId}")
	public ResponseEntity<ApiResponse<HabitRecordResponse>> updateHabitRecord(@Valid @RequestBody HabitRecordRequest req,@PathVariable Long habitRecordId){
		HabitRecordResponse habitRecord=habitRecordService.updateHabitRecord(habitRecordId,req);
		return ResponseUtil.success(habitRecord, "Habit record updated", HttpStatus.OK, Map.of("endpoint","/habit_records"));
	}
	
	@DeleteMapping("/{habitRecordId}")
	public ResponseEntity<ApiResponse<HabitRecordResponse>> deleteHabitRecord(@PathVariable Long habitRecordId){
		HabitRecordResponse habitRecord=habitRecordService.deleteHabitRecord(habitRecordId);
		return ResponseUtil.success(habitRecord, "Habit record deleted", HttpStatus.OK, Map.of("endpoint","/habit_records"));
	}
}
