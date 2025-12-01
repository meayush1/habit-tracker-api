package com.ayush.habittracker.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ayush.habittracker.dto.request.HabitRequest;
import com.ayush.habittracker.dto.response.HabitResponse;
import com.ayush.habittracker.exception.HabitNotFoundException;
import com.ayush.habittracker.exception.UserNotFoundException;
import com.ayush.habittracker.model.Category;
import com.ayush.habittracker.model.Habit;
import com.ayush.habittracker.model.User;
import com.ayush.habittracker.repository.HabitRepository;
import com.ayush.habittracker.repository.UserRepository;
import com.ayush.habittracker.security.util.AuthUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class HabitService {

	@Autowired
	HabitRepository repo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	AuthUtil authUtil;

	public HabitResponse createHabit(Long userId, HabitRequest req ) {
		// check ownership
		authUtil.checkOwnership( authUtil.getLoggedInUserId(), userId);
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

		// map habit request-> Habit
		Habit newHabit = mapper.map(req, Habit.class);

		// mapper can map userId->id(habit)
		newHabit.setId(null);
		newHabit.setUser(user);
		// add timestamps & save into DB
		newHabit.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		newHabit.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

		Habit savedHabit = repo.save(newHabit);

		// return habitResponse
		return mapper.map(savedHabit, HabitResponse.class);
	}

	public HabitResponse getHabitById(Long id ) {
		Habit habit = repo.findById(id).orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));
		// check ownership
		authUtil.checkOwnership( authUtil.getLoggedInUserId(), habit.getUser().getId());
		return mapper.map(habit, HabitResponse.class);
	}

	public Page<HabitResponse> getAllHabits(int page, int size, String sortBy, String direction) {
		Sort sort;
		if (direction.equalsIgnoreCase("desc")) {
			sort = Sort.by(sortBy).descending();
		} else {
			sort = Sort.by(sortBy).ascending();
		}

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Habit> allHabits = repo.findAll(pageable);

		return allHabits.map((habit) -> mapper.map(habit, HabitResponse.class));

	}

	public List<HabitResponse> searchHabits(String keyword ) {
		List<Habit> habits = repo.findByTitleContainingIgnoreCase(keyword);
		return habits.stream().map((habit) -> mapper.map(habit, HabitResponse.class)).toList();
	}

	public List<HabitResponse> filterByCategory(Category category ) {
		List<Habit> habits = repo.findByCategory(category);
		return habits.stream().map((habit) -> mapper.map(habit, HabitResponse.class)).toList();
	}

	public HabitResponse updateHabit(Long id, HabitRequest req ) {
		Habit habit = repo.findById(id).orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));
		// check ownership
		authUtil.checkOwnership( authUtil.getLoggedInUserId(), habit.getUser().getId());
		mapper.map(req, habit);
		habit.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		Habit updatedHabit = repo.save(habit);
		return mapper.map(updatedHabit, HabitResponse.class);
	}

	public HabitResponse deleteHabit(Long id ) {
		Habit habit = repo.findById(id).orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));
		// check ownership
		authUtil.checkOwnership( authUtil.getLoggedInUserId(), habit.getUser().getId());
		repo.delete(habit);
		return mapper.map(habit, HabitResponse.class);
	}

}
