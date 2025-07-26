package com.spring.jwt.service;

import com.spring.jwt.dto.PayslipDTO;

public interface PayslipPdfService {
    byte[] generatePayslipPdf(PayslipDTO dto);
    byte[] generatePayslipPdfFromHtml(String html);
}
