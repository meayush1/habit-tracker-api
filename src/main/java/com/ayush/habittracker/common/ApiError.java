package com.ayush.habittracker.common;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API error response")
@Data
@AllArgsConstructor
@Builder
public class ApiError {

    @Schema(example = "2026-01-04T18:40:21Z")
	private LocalDateTime timeStamp;
    @Schema(example = "404")
	private int statusCode;
    @Schema(example = "NOT_FOUND")
	private String errorCode;
    @Schema(example = "User not found")
	private String message;
    @Schema(example = "/users/123")
	private String path;
	
}
