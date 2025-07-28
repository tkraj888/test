package com.spring.jwt.Attandance.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class AttendanceSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate date;

    private Double totalWorkingHours;

    private String workMode;

    private String attendanceStatus; // Present, Half-Day, Absent
}
