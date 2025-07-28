package com.spring.jwt.Attandance.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class AttendanceEmpCreateDto {
    private Long userId;
    private String workMode;

}
