package com.spring.jwt.Attandance.Dto;

import lombok.Data;

import java.util.List;

@Data
public class MonthlyAttendanceReportDto {
    private Long userId;
    private String month; // Format: "YYYY-MM"
    private int presentDays;
    private int halfDays;
    private int absentDays;
    private double totalPresentEquivalent; // present + (halfDays / 2)
    private List<AttendanceSummaryDailyDto> dailySummaries;
}
