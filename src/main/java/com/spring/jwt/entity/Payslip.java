package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @PrePersist
    @PreUpdate
    public void roundFields() {
        basicPay = round(basicPay);
        hra = round(hra);
        specialAllowances = round(specialAllowances);
        travelAllowances = round(travelAllowances);
        da = round(da);
        yearlyIncentive = round(yearlyIncentive);
        totalDeductions = round(totalDeductions);
        totalEarnings = round(totalEarnings);
        netSalary = round(netSalary);
    }

    private Double round(Double value) {
        if (value == null) return null;
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
