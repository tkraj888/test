package com.spring.jwt.Attandance.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class AttendanceEmp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate date;

    private LocalTime loginTime;

    private LocalTime logoutTime;

    private String workMode; // WFH, WFO, Leave

    private Double workingHours; // Calculated
}
