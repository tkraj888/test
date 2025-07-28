package com.spring.jwt.Attandance.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceSummaryDto {
    private Long id;
    private Long userId;
    private LocalDate date;
    private Double totalWorkingHours;
    private String workMode;
    private String attendanceStatus;
}
