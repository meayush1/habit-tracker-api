package com.ayush.habittracker.exception;

public class WrongPasswordException extends RuntimeException {
	public WrongPasswordException(String msg) {
		super(msg);
	}
}
