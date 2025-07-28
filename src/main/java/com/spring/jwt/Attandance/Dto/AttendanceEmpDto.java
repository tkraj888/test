package com.spring.jwt.Attandance.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AttendanceEmpDto {
    private Long id;
    private Long userId;
    private LocalDate date;
    private LocalTime loginTime;
    private LocalTime logoutTime;
    private String workMode;
    @JsonIgnore
    private Double workingHours;
}
