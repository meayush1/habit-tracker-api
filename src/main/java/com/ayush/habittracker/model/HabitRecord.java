package com.ayush.habittracker.model;

import java.time.LocalDate;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Habit habit;

    @NotNull()
    private LocalDate date;       // day-specific record
    
    @NotNull()
    @Enumerated(EnumType.STRING)
    private HabitStatus status;        // DONE, MISSED
    
    private String note;          // optional
    
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    
}
