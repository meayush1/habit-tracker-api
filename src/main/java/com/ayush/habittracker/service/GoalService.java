package com.ayush.habittracker.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ayush.habittracker.dto.request.GoalRequest;
import com.ayush.habittracker.dto.response.GoalResponse;
import com.ayush.habittracker.exception.GoalNotFoundException;
import com.ayush.habittracker.exception.UserNotFoundException;
import com.ayush.habittracker.model.Category;
import com.ayush.habittracker.model.Goal;
import com.ayush.habittracker.model.User;
import com.ayush.habittracker.repository.GoalRepository;
import com.ayush.habittracker.repository.UserRepository;
import com.ayush.habittracker.security.util.AuthUtil;

@Service
public class GoalService {
	@Autowired
	GoalRepository goalRepo;
	@Autowired
	ModelMapper mapper;
	@Autowired
	UserRepository userRepo;

	@Autowired
	AuthUtil authUtil;
	private static final Logger log = LoggerFactory.getLogger(GoalService.class);

	public GoalResponse createGoal(GoalRequest req) {
		Goal goal = mapper.map(req, Goal.class);
		goal.setId(null);

		User user = userRepo.findById(req.getUserId()).orElseThrow(() -> {

			return new UserNotFoundException("User not found with id: " + req.getUserId());
		});
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), user.getId());
		goal.setUser(user);

		goal = goalRepo.save(goal);

		GoalResponse goalRes = mapper.map(goal, GoalResponse.class);

		double pct = goal.getTargetValue() == 0 ? 0.0 : (goal.getCurrentProgress() * 100.0 / goal.getTargetValue());

		goalRes.setProgressPercentage(pct);

		return goalRes;
	}

	public GoalResponse getGoalById(Long id) {
		Goal goal = goalRepo.findById(id)
				.orElseThrow(() -> new GoalNotFoundException("Goal not gound with id : " + id));
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), goal.getUser().getId());
		GoalResponse goalRes = mapper.map(goal, GoalResponse.class);
		goalRes.setProgressPercentage(getProgressPercentage(id));
		return goalRes;
	}

	public GoalResponse updateGoal(Long id, GoalRequest req) {
		Goal goal = goalRepo.findById(id)
				.orElseThrow(() -> new GoalNotFoundException("Goal not gound with id : " + id));
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), goal.getUser().getId());
		mapper.map(req, goal);
		goal.setId(id);
		if (req.getUserId() != null) {
			User user = userRepo.findById(req.getUserId())
					.orElseThrow(() -> new UserNotFoundException("User not found with id: " + req.getUserId()));
			goal.setUser(user);
		}
		goal = goalRepo.save(goal);

		GoalResponse goalRes = mapper.map(goal, GoalResponse.class);
		goalRes.setProgressPercentage(getProgressPercentage(id));
		return goalRes;
	}

	public String deleteGoal(Long id) {
		Goal goal = goalRepo.findById(id)
				.orElseThrow(() -> new GoalNotFoundException("Goal not gound with id : " + id));
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), goal.getUser().getId());
		goalRepo.deleteById(id);
		return "Goal with id:" + id + " deleted successfully";
	}

	public List<GoalResponse> getGoalsByUser(Long userId) {
		List<Goal> goals = goalRepo.findByUserId(userId);
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), userId);
		List<GoalResponse> goalRes = goals.stream().map(goal -> {
			GoalResponse res = mapper.map(goal, GoalResponse.class);
			res.setProgressPercentage(getProgressPercentage(goal.getId()));
			return res;
		}).toList();
		return goalRes;
	}

	public List<GoalResponse> getGoalsByCategory(Category category) {
		List<Goal> goals = goalRepo.findByCategory(category);
		List<GoalResponse> goalRes = goals.stream().map(goal -> {
			GoalResponse res = mapper.map(goal, GoalResponse.class);
			res.setProgressPercentage(getProgressPercentage(goal.getId()));
			return res;
		}).toList();
		return goalRes;
	}

	public double getProgressPercentage(Long id) {
		Goal goal = goalRepo.findById(id)
				.orElseThrow(() -> new GoalNotFoundException("Goal not gound with id : " + id));
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), goal.getUser().getId());
		double pct = goal.getTargetValue() == 0 ? 0.0 : goal.getCurrentProgress() * 100.0 / goal.getTargetValue();

		return pct;
	}
}
