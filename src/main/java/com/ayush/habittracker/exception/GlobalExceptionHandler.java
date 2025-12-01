package com.ayush.habittracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.ayush.habittracker.common.ApiError;
import com.ayush.habittracker.common.ResponseUtil;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

	// Handle validation error
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationError(MethodArgumentNotValidException ex, WebRequest req) {
		String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
		return ResponseUtil.error("VAL_400", msg, HttpStatus.BAD_REQUEST, req.getDescription(false));
	}

	// Handle User not found error
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundError(UserNotFoundException ex, WebRequest req) {
		return ResponseUtil.error("USR_404", ex.getMessage(), HttpStatus.NOT_FOUND, req.getDescription(false));
	}

	// Handle Email already exists error
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleEmailAlreadyExistsError(EmailAlreadyExistsException ex, WebRequest req) {
		return ResponseUtil.error("USR_400", ex.getMessage(), HttpStatus.CONFLICT, req.getDescription(false));
	}

	@ExceptionHandler(HabitNotFoundException.class)
	public ResponseEntity<ApiError> HabitNotFoundException(HabitNotFoundException ex, WebRequest req) {
		return ResponseUtil.error("HBT_404", ex.getMessage(), HttpStatus.NOT_FOUND, req.getDescription(false));
	}

	@ExceptionHandler(HabitRecordNotFoundException.class)
	public ResponseEntity<ApiError> HabitRecordNotFoundException(HabitRecordNotFoundException ex, WebRequest req) {
		return ResponseUtil.error("HBT_REC_404", ex.getMessage(), HttpStatus.NOT_FOUND, req.getDescription(false));
	}

	@ExceptionHandler(GoalNotFoundException.class)
	public ResponseEntity<ApiError> goalNotFoundException(GoalNotFoundException ex, WebRequest req) {
		return ResponseUtil.error("GOL_404", ex.getMessage(), HttpStatus.NOT_FOUND, req.getDescription(false));
	}

	@ExceptionHandler(HabitRecordAlreadyExistsException.class)
	public ResponseEntity<ApiError> habitRecordAlreadyExistsEroor(HabitRecordAlreadyExistsException ex,
			WebRequest req) {
		return ResponseUtil.error("HBT_REC_400", ex.getMessage(), HttpStatus.CONFLICT, req.getDescription(false));
	}

	// Handle wrong password  error
	@ExceptionHandler(WrongPasswordException.class)
	public ResponseEntity<ApiError> wrongPasswordException(Exception ex, WebRequest req) {
		return ResponseUtil.error("LONIN_400", ex.getMessage(), HttpStatus.EXPECTATION_FAILED,
				req.getDescription(false));
	}

	//handle forbidden exception
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex, WebRequest req) {
	    return ResponseUtil.error("FORBIDDEN_403", ex.getMessage(), HttpStatus.FORBIDDEN, req.getDescription(false));
	}

	// Handle general error
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneralError(Exception ex, WebRequest req) {
		return ResponseUtil.error("GEN_500", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
				req.getDescription(false));
	}

}
