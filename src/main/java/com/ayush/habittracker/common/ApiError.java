package com.ayush.habittracker.common;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApiError {
	private LocalDateTime timeStamp;
	private int statusCode;
	private String errorCode;
	private String message;
	private String path;
	
}
