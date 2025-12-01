package com.ayush.habittracker.model;
/*
 * id
userId (ManyToOne)
title
description
category
frequency (DAILY / WEEKLY)
isActive
createdAt
updatedAt

 */

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Relationship: Many habits belong to 1 user

    private String title;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private Category category;
    
    
    @Enumerated(EnumType.STRING)
    private HabitFrequency frequency;  // DAILY, WEEKLY

    private boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
