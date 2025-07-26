package com.spring.jwt.service;

import com.spring.jwt.dto.PayslipDTO;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PayslipHtmlGenerator {

    public static String loadAndFillTemplate(PayslipDTO dto) {
        InputStream is = PayslipHtmlGenerator.class.getResourceAsStream("/templates/payslipTemplate.html");
        if (is == null) throw new RuntimeException("Template not found");

        String html = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A").next();

        return html
                .replace("${month}", dto.getMonth())
                .replace("${year}", dto.getYear().toString())
                .replace("${name}", dto.getName())
                .replace("${employeeId}", dto.getEmployeeId())
                .replace("${designation}", dto.getDesignation())
                .replace("${bankName}", dto.getBankName())
                .replace("${department}", dto.getDepartment())
                .replace("${bankAccountNumber}", dto.getBankAccountNumber())
                .replace("${location}", dto.getLocation())
                .replace("${pan}", dto.getPan())
                .replace("${dateOfJoining}", dto.getDateOfJoining())
                .replace("${absentDays}", dto.getAbsentDays().toString())
                .replace("${basicPay}", String.format("%.2f", dto.getBasicPay()))
                .replace("${hra}", String.format("%.2f", dto.getHra()))
                .replace("${da}", String.format("%.2f", dto.getDa()))
                .replace("${travelAllowances}", String.format("%.2f", dto.getTravelAllowances()))
                .replace("${specialAllowances}", String.format("%.2f", dto.getSpecialAllowances()))
                .replace("${totalEarnings}", String.format("%.2f", dto.getTotalEarnings()))
                .replace("${totalDeductions}", String.format("%.2f", dto.getTotalDeductions()))
                .replace("${netSalary}", String.format("%.2f", dto.getNetSalary()));
    }
}
