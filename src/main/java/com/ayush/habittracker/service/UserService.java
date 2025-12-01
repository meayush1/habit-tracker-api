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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ayush.habittracker.dto.request.UserRequest;
import com.ayush.habittracker.dto.response.UserResponse;
import com.ayush.habittracker.exception.EmailAlreadyExistsException;
import com.ayush.habittracker.exception.UserNotFoundException;
import com.ayush.habittracker.model.Habit;
import com.ayush.habittracker.model.Role;
import com.ayush.habittracker.model.User;
import com.ayush.habittracker.repository.HabitRepository;
import com.ayush.habittracker.repository.UserRepository;
import com.ayush.habittracker.security.util.AuthUtil;

@Service
public class UserService {

	@Autowired
	ModelMapper mapper;

	@Autowired
	UserRepository repo;

	@Autowired
	HabitRepository habitRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthUtil authUtil;

	// create user
	public UserResponse createUser(UserRequest userReq) {
		// check if the email is already exists or not
		if (repo.existsByEmail(userReq.getEmail())) {
			throw new EmailAlreadyExistsException("Email already registered: " + userReq.getEmail());
		}

		// change userRequest->user
		User user = mapper.map(userReq, User.class);

		// set time stamp and save user in DB
		user.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
//		user.setRole(Role.ADMIN);
		User savedUser = repo.save(user);

		// again change user -> userResponse
		UserResponse userRes = mapper.map(savedUser, UserResponse.class);
		// return userReponse
		return userRes;

	}

	// fetch one user by email
	public UserResponse getUserByEmail(String email) {
		// find user from DB
		User user = repo.findByEmail(email);
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), user.getId());
		if (user == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		}

		// return user->userResponse
		return mapper.map(user, UserResponse.class);
	}

	// fetch one user by id
	public UserResponse getUserById(Long id) {
		// find user from DB
		User user = repo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), user.getId());
		// return user->userResponse
		return mapper.map(user, UserResponse.class);
	}

	// fetch all users
	public Page<UserResponse> getAllUsers(int page, int size, String sortBy) {

		// 1. Create sorting rules
		Sort sort = Sort.by(sortBy).ascending();

		// 2. Create Pageable object
		Pageable pageable = PageRequest.of(page, size, sort);

		// 3. Fetch paginated users
		Page<User> userPage = repo.findAll(pageable);

		// 4. Convert each User → UserResponse using ModelMapper
		Page<UserResponse> responsePage = userPage.map(user -> mapper.map(user, UserResponse.class));

		return responsePage;
	}

	// find by name
	public List<UserResponse> searchUser(String keyword) {
		List<User> users = repo.findByNameContainingIgnoreCase(keyword);
		return users.stream().map((user) -> mapper.map(user, UserResponse.class)).toList();
	}

	// update user
	public UserResponse updateUser(Long id, UserRequest userReq) {
		// find the user by id
		User user = repo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), user.getId());
		// update the user
		if (userReq.getName() != null)
			user.setName(userReq.getName());
		if (userReq.getEmail() != null)
			user.setEmail(userReq.getEmail());
		if (userReq.getPassword() != null)
			user.setPassword(userReq.getPassword());
		user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
		User savedUser = repo.save(user);
		// send UserResponse
		return mapper.map(savedUser, UserResponse.class);
	}

	// delete user
	public String deleteUser(Long id) {
		// find the user by id
		User user = repo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		// check ownership
		authUtil.checkOwnership(authUtil.getLoggedInUserId(), user.getId());
		List<Habit> habits = habitRepo.findByUserId(id);
		habitRepo.deleteAll(habits);
		repo.delete(user);

		repo.delete(user);
		return "User with id " + id + ", deleted succesfully";
	}
}
