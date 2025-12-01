package com.ayush.habittracker.exception;

public class HabitRecordAlreadyExistsException extends RuntimeException {
	public HabitRecordAlreadyExistsException(String msg) {
		super(msg);
	}
}
