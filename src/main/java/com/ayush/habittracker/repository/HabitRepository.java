package com.ayush.habittracker.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayush.habittracker.model.Category;
import com.ayush.habittracker.model.Habit;

@Repository
public interface HabitRepository extends JpaRepository<Habit,Long>{
	/*
	 * findByUserId

findByTitleContainingIgnoreCase

findByCategory

pagination
	 */
	
	public Page<Habit> findByUserId(Long userId,Pageable pageable);
	public List<Habit> findByUserId(Long userId);
	public List<Habit> findByTitleContainingIgnoreCase(String title);
	public List<Habit> findByCategory(Category category);

	
}
