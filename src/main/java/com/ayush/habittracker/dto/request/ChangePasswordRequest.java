package com.ayush.habittracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
	@NotBlank
	@Email(message = "Invalid email format")
	private String email;
	@NotBlank
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,20}$", message = "Password must contain upper, lower, digit, special char, 8–20 chars")
	private String newPassword;
	@NotBlank
	private String verifyPassword;
	
	private boolean isEmailVerified=false;
}
