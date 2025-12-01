package com.ayush.habittracker.analytics;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ayush.habittracker.dto.response.HabitResponse;
import com.ayush.habittracker.dto.response.analytics.DashboardResponse;
import com.ayush.habittracker.dto.response.analytics.MonthlySummaryResponse;
import com.ayush.habittracker.dto.response.analytics.WeeklySummaryResponse;
import com.ayush.habittracker.exception.HabitNotFoundException;
import com.ayush.habittracker.model.Habit;
import com.ayush.habittracker.model.HabitFrequency;
import com.ayush.habittracker.model.HabitRecord;
import com.ayush.habittracker.model.HabitStatus;
import com.ayush.habittracker.repository.HabitRecordRepository;
import com.ayush.habittracker.repository.HabitRepository;
import com.ayush.habittracker.security.util.AuthUtil;

@Service
public class HabitAnalyticsService {

	@Autowired
	HabitRecordRepository habitRecordRepo;

	@Autowired
	HabitRepository habitRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	AuthUtil authUtil;

	// 1. Calculate current streak
	public int getCurrentStreak(Long habitId) {
		// find the all habit record for this habitId
		List<HabitRecord> habitRecord = habitRecordRepo.findByHabitIdOrderByDateAsc(habitId);
		if (habitRecord.size() != 0) {
			// check ownership
			authUtil.checkOwnership(authUtil.getLoggedInUserId(), habitRecord.get(0).getHabit().getUser().getId());
		}
		// traverse those habitRecord from last to fast and set the streak
		int streak = 0;
		LocalDate yesterday = LocalDate.now();
		int len = habitRecord.size();
		if (len == 0)
			return 0;
		for (int i = len - 1; i >= 0; i--) {
			if (habitRecord.get(i).getDate().equals(yesterday) && habitRecord.get(i).getStatus() == HabitStatus.DONE) {
				streak++;
				yesterday = yesterday.minusDays(1);
			} else {
				break;
			}
		}

		return streak;
	}

	// 2. Calculate longest streak
	public int getLongestStreak(Long habitId) {
		// find the all habit record for this habitId
		List<HabitRecord> habitRecord = habitRecordRepo.findByHabitIdOrderByDateAsc(habitId);
		if (habitRecord.size() != 0) {
			// check ownership
			authUtil.checkOwnership(authUtil.getLoggedInUserId(), habitRecord.get(0).getHabit().getUser().getId());
		}
		// traverse those habitRecord from last to fast and set the streak

		int len = habitRecord.size();
		if (len == 0)
			return 0;
		int longest = 0;
		int current = 0;
		LocalDate prevDate = null;

		for (HabitRecord r : habitRecord) {
			if (r.getStatus() == HabitStatus.DONE) {
				if (prevDate != null && r.getDate().equals(prevDate.plusDays(1))) {
					current++;
				} else {
					current = 1;
				}
				longest = Math.max(longest, current);
			} else {
				current = 0;
			}
			prevDate = r.getDate();
		}

		return longest;

	}

	// 3. Completion rate (percentage)
	public double getCompletionRate(Long habitId) {
		List<HabitRecord> habitRecords = habitRecordRepo.findByHabitId(habitId);
		if (habitRecords.size() != 0) {
			// check ownership
			authUtil.checkOwnership(authUtil.getLoggedInUserId(), habitRecords.get(0).getHabit().getUser().getId());
		}
		long completed = habitRecords.stream().filter(r -> r.getStatus() == HabitStatus.DONE).count();
		return (completed * 100.0) / habitRecords.size();
	}

	// 4. Weekly summary
	public WeeklySummaryResponse getWeeklySummary(Long habitId) {

		LocalDate start = LocalDate.now().with(DayOfWeek.MONDAY);
		LocalDate end = start.plusDays(6);
		LocalDate current = LocalDate.now();
		List<HabitRecord> habitRecords = habitRecordRepo.findByHabitIdAndDateBetweenOrderByDateAsc(habitId, start, end);
		if (habitRecords.size() != 0) {
			// check ownership
			authUtil.checkOwnership(authUtil.getLoggedInUserId(), habitRecords.get(0).getHabit().getUser().getId());
		}
		int doneRecords = 0;
		int len = habitRecords.size();
		Map<LocalDate, HabitStatus> map = new LinkedHashMap<>();

		for (int i = len - 1; i >= 0; i--) {
			if (habitRecords.get(i).getStatus() == HabitStatus.DONE) {
				doneRecords++;
			}
			map.put(habitRecords.get(i).getDate(), habitRecords.get(i).getStatus());
		}

		int totalDate = 0;

		Habit habit = habitRepo.findById(habitId)
				.orElseThrow(() -> new HabitNotFoundException("Habit not found with habit id: " + habitId));
		if (habit.getFrequency().equals(HabitFrequency.DAILY)) {
			totalDate = (int) ChronoUnit.DAYS.between(start, end) + 1;
			;
		} else {
			totalDate = 1;
		}
		WeeklySummaryResponse res = new WeeklySummaryResponse();
		// set values
		res.setTotalDays(len);
		res.setCompletedDays(doneRecords);
		res.setMissedDays(len - doneRecords);
		if (len == 0)
			res.setCompletionRate(0);
		else
			res.setCompletionRate((doneRecords / (len * 1.0)) * 100);

		res.setDailyBreakdown(map);

		return res;
	}

	// 5. Monthly summary
	public MonthlySummaryResponse getMonthlySummary(Long habitId) {
		LocalDate start = LocalDate.now().withDayOfMonth(1);
		LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		LocalDate current = LocalDate.now();
		List<HabitRecord> habitRecords = habitRecordRepo.findByHabitIdAndDateBetweenOrderByDateAsc(habitId, start, end);
		if (habitRecords.size() != 0) {
			// check ownership
			authUtil.checkOwnership(authUtil.getLoggedInUserId(), habitRecords.get(0).getHabit().getUser().getId());
		}
		int doneRecords = 0;
		int len = habitRecords.size();
		Map<LocalDate, HabitStatus> map = new LinkedHashMap<>();

		for (int i = len - 1; i >= 0; i--) {
			if (habitRecords.get(i).getStatus() == HabitStatus.DONE) {
				doneRecords++;
			}
			map.put(habitRecords.get(i).getDate(), habitRecords.get(i).getStatus());
		}

		int totalDate = 0;

		Habit habit = habitRepo.findById(habitId)
				.orElseThrow(() -> new HabitNotFoundException("Habit not found with habit id: " + habitId));
		if (habit.getFrequency().equals(HabitFrequency.DAILY)) {
			totalDate = (int) ChronoUnit.DAYS.between(start, end) + 1;
		} else if (habit.getFrequency().equals(HabitFrequency.WEEKLY)) {
			totalDate = 4;
		} else {
			totalDate = 1;
		}
		MonthlySummaryResponse res = new MonthlySummaryResponse();

		// set values
		res.setTotalDays(len);
		res.setCompletedDays(doneRecords);
		res.setMissedDays(len - doneRecords);
		if (len == 0)
			res.setCompletionRate(0);
		else
			res.setCompletionRate((doneRecords / (len * 1.0)) * 100);

		res.setDailyBreakdown(map);

		return res;
	}

	// 6. Dashboard for User
	public DashboardResponse getUserDashboard(Long userId) {

		// get all habits for this user(check if active)
		List<Habit> allHabits = habitRepo.findByUserId(userId);

		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), userId);

		int activeHabits = 0, len = allHabits.size();
		double CompletionRate = 0.00, maxCompletionRate = 0.00, minCompletionRate = 100.00, totalCompletionRate = 0.00;
		Habit bestHabit = null, worstHabit = null;

		// compute completion rate for each habit(decide best and worst habit)
		for (int i = 0; i < len; i++) {
			if (allHabits.get(i).isActive() == true)
				activeHabits++;
			CompletionRate = getCompletionRate(allHabits.get(i).getId());
			totalCompletionRate += CompletionRate;
			if (CompletionRate > maxCompletionRate) {
				maxCompletionRate = CompletionRate;
				bestHabit = allHabits.get(i);
			}
			if (CompletionRate < minCompletionRate) {
				minCompletionRate = CompletionRate;
				worstHabit = allHabits.get(i);
			}
		}
		DashboardResponse res = new DashboardResponse();
		res.setActiveHabits(activeHabits);
		res.setAllHabits(allHabits.stream().map((habit) -> mapper.map(habit, HabitResponse.class)).toList());
		res.setBestHabit(bestHabit != null ? mapper.map(bestHabit, HabitResponse.class) : null);
		res.setWorstHabit(worstHabit != null ? mapper.map(worstHabit, HabitResponse.class) : null);
		if (len == 0)
			res.setOverallSuccessRate(0);
		else
			res.setOverallSuccessRate(totalCompletionRate / len);

		res.setTotalHabits(len);

		return res;

	}
}
