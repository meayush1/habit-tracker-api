package com.ayush.habittracker.exception;

public class HabitNotFoundException extends RuntimeException {
	public HabitNotFoundException(String msg) {
		super(msg);
	}
}
