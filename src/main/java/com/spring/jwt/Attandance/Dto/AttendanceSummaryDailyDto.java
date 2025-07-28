package com.spring.jwt.Attandance.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceSummaryDailyDto {
    private LocalDate date;
    private Double totalWorkingHours;
    private String attendanceStatus; // e.g., Present, Absent, Half Day
}
