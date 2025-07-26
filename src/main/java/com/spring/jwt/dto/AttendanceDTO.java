package com.spring.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private String employeeId;
    private String month;
    private Integer year;
    private Integer presentDays;
    private Integer absentDays;
    private Integer totalDays;
}
