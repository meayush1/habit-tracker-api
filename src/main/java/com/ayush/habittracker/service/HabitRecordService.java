package com.ayush.habittracker.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ayush.habittracker.dto.request.HabitRecordRequest;
import com.ayush.habittracker.dto.response.HabitRecordResponse;
import com.ayush.habittracker.exception.HabitNotFoundException;
import com.ayush.habittracker.exception.HabitRecordAlreadyExistsException;
import com.ayush.habittracker.exception.HabitRecordNotFoundException;
import com.ayush.habittracker.model.Habit;
import com.ayush.habittracker.model.HabitRecord;
import com.ayush.habittracker.repository.HabitRecordRepository;
import com.ayush.habittracker.repository.HabitRepository;
import com.ayush.habittracker.security.util.AuthUtil;

@Service
public class HabitRecordService {

    @Autowired
    ModelMapper mapper;
	@Autowired
	HabitRecordRepository habitRecordRepo;
	
	@Autowired 
	HabitRepository habitRepo;
	@Autowired AuthUtil authUtil;
	public HabitRecordResponse createHabitRecord(HabitRecordRequest req) {
		//check if habit record for this habitId + date already exists or not
		boolean isAlreadyExistsHabit=habitRecordRepo.existsByHabitIdAndDate(req.getHabitId(), req.getDate());
		if(isAlreadyExistsHabit) {
			throw new HabitRecordAlreadyExistsException("Already a habit record exists for habitId: "+req.getHabitId()+" and date: "+req.getDate());
		}
		
		Habit habit = habitRepo.findById(req.getHabitId())
		        .orElseThrow(() -> new HabitNotFoundException("Habit not found"));
		// check ownership
		authUtil.checkOwnership( authUtil.getLoggedInUserId(), habit.getUser().getId());
		
		HabitRecord newHabitRecord=mapper.map(req, HabitRecord.class);
		newHabitRecord.setId(null);
		
		
		newHabitRecord.setHabit(habit);
		newHabitRecord.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		newHabitRecord.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		
		
		HabitRecord savedHabitRecord=habitRecordRepo.save(newHabitRecord);
		
		return mapper.map(savedHabitRecord, HabitRecordResponse.class);
	}
	
	public List<HabitRecordResponse> findAllHabitRecordsOfHabit(Long habitId){
		List<HabitRecord> habitRecords=habitRecordRepo.findByHabitId(habitId);
		if(habitRecords.size()!=0) {
			// check ownership
			authUtil.checkOwnership( authUtil.getLoggedInUserId(), habitRecords.get(0).getHabit().getUser().getId());
		}
		return habitRecords.stream().map((habitRecord)->mapper.map(habitRecord,HabitRecordResponse.class)).toList();
	}
	
	public List<HabitRecordResponse> findAllHabitRecordsByDate(LocalDate date){
		List<HabitRecord> habitRecords=habitRecordRepo.findByDate(date);
		
		return habitRecords.stream().map((habitRecord)->mapper.map(habitRecord,HabitRecordResponse.class)).toList();
	}
	
	public List<HabitRecordResponse> findAllHabitRecordsByHabitIdAndDateBetween(Long habitId,LocalDate startDate,LocalDate endDate){
		List<HabitRecord> habitRecords=habitRecordRepo.findByHabitIdAndDateBetween(habitId, startDate, endDate);
		if(habitRecords.size()!=0) {
			// check ownership
			authUtil.checkOwnership( authUtil.getLoggedInUserId(), habitRecords.get(0).getHabit().getUser().getId());
		}
		return habitRecords.stream().map((habitRecord)->mapper.map(habitRecord,HabitRecordResponse.class)).toList();
	}
	
	public HabitRecordResponse updateHabitRecord(Long habitRecordId,HabitRecordRequest req) {
		HabitRecord habitRecord=habitRecordRepo.findById(habitRecordId).orElseThrow(()->new HabitRecordNotFoundException("Habit record not found with id: " + habitRecordId));
		// check ownership
		authUtil.checkOwnership( authUtil.getLoggedInUserId(), habitRecord.getHabit().getUser().getId());
		mapper.map(req, habitRecord);
		habitRecord.setId(habitRecordId);
		habitRecord.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		
		HabitRecord savedHabitRecord=habitRecordRepo.save(habitRecord);
		
		return mapper.map(savedHabitRecord, HabitRecordResponse.class);
	}
	
	public HabitRecordResponse deleteHabitRecord(Long id) {
		HabitRecord habitRecord=habitRecordRepo.findById(id).orElseThrow(() -> new HabitRecordNotFoundException("Habit record not found with id: "+id));
		// check ownership
		authUtil.checkOwnership( authUtil.getLoggedInUserId(), habitRecord.getHabit().getUser().getId());
		habitRecordRepo.delete(habitRecord);
		return mapper.map(habitRecord, HabitRecordResponse.class);
	}
	
}
