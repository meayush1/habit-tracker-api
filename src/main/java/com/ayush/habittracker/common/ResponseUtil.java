package com.ayush.habittracker.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(
            T data,
            String message,
            HttpStatus status,
            Map<String, Object> metadata
    ) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .statusCode(status.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now(ZoneId.of("UTC")))
                .metadata(metadata)
                .errorCode(null)
                .build();

        return new ResponseEntity<>(response, status);
    }
    public static  ResponseEntity<ApiError> error(
            String errorCode,
            String message,
            HttpStatus status,
            String path
    ) {
        ApiError error=ApiError.builder().message(message).errorCode(errorCode).path(path).statusCode(status.value()).timeStamp(LocalDateTime.now(ZoneId.of("UTC"))).build();
        return new ResponseEntity<>(error,status);
    }
}
