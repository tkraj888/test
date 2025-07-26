package com.spring.jwt.dto;

import lombok.Data;

@Data
public class PayslipDTO {
    private Integer userId;
    private String month;
    private Integer year;


    private String name;
    private String designation;
    private String department;
    private String location;
    private String dateOfJoining;
    private String employeeId;
    private String bankName;
    private String bankAccountNumber;
    private String pan;

    private Double basicPay;
    private Double hra;
    private Double specialAllowances;
    private Double travelAllowances;
    private Double da;
    private Double yearlyIncentive;
    private Double totalDeductions;

    private Integer totalDays;
    private Integer presentDays;
    private Integer absentDays;

    private Double totalEarnings;
    private Double netSalary;
}
