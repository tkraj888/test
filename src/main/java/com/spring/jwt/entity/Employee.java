package com.spring.jwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String designation;
    private String department;
    private String location;
    private String dateOfJoining;
    private String employeeId;
    private String bankName;
    private String bankAccountNumber;
    private String pan;
    private String packageAmount;

    private Double basicPay;
    private Double hra;
    private Double specialAllowances;
    private Double travelAllowances;
    private Double da;
    private Double yearlyIncentive;
    private Integer userId;
    private Double totalDeductions;
}
