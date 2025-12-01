package com.ayush.habittracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayush.habittracker.model.HabitRecord;

@Repository
public interface HabitRecordRepository extends JpaRepository<HabitRecord,Long> {
	public List<HabitRecord> findByHabitId(Long habitId);
	public List<HabitRecord> findByDate(LocalDate date);
	public HabitRecord findByHabitIdAndDate(Long habitId,LocalDate date);
	public boolean existsByHabitIdAndDate(Long habitId,LocalDate date);
	public List<HabitRecord> findByHabitIdAndDateBetween(Long habitId,LocalDate startDate,LocalDate endDate);
	
	//Find all habit record for a particular habit in ascending order by date
	public List<HabitRecord> findByHabitIdOrderByDateAsc(Long habitId);
	
	//fetch between two dates
	public List<HabitRecord> findByHabitIdAndDateBetweenOrderByDateAsc(Long habitId,LocalDate startDate,LocalDate endDate);
}
