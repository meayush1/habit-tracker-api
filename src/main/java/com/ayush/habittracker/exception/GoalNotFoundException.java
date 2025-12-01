package com.ayush.habittracker.exception;

public class GoalNotFoundException extends RuntimeException {
	public GoalNotFoundException(String msg) {
		super(msg);
	}
}
