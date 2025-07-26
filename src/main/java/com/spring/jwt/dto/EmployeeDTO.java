package com.spring.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
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
