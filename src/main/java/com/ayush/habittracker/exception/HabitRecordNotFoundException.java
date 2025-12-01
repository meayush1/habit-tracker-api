package com.ayush.habittracker.exception;

public class HabitRecordNotFoundException extends RuntimeException {
	public HabitRecordNotFoundException(String msg) {
		super(msg);
	}
}
