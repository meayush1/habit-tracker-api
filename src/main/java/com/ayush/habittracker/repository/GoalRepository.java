package com.ayush.habittracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ayush.habittracker.model.Category;
import com.ayush.habittracker.model.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal,Long> {
	public List<Goal> findByUserId(Long userId);
	public List<Goal> findByCategory(Category category);
	public List<Goal> findByTargetDateBefore(LocalDate date);
}
