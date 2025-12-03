package com.ayush.habittracker.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String otp;
	
	@Column(nullable=false)
	private String email;
	
	@Column(nullable=false)
	private LocalDateTime expiredAt;
	
	@Column(nullable = false)
    private LocalDateTime lastSentAt;
	
	@Column(nullable=false)
	private int attempts=0;
	
	@Column(nullable=false)
	private OtpType otpType=OtpType.REGISTRATION;
	boolean verifiedForPasswordReset = false;

}
