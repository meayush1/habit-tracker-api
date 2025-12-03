package com.ayush.habittracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.habittracker.model.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {
	public Otp findByEmail(String email);
	public void deleteByEmail(String email);
}
