package com.spring.jwt.controller;

import com.spring.jwt.dto.AttendanceDTO;
import com.spring.jwt.dto.PayslipDTO;
import com.spring.jwt.entity.Payslip;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.PayslipRepository;
import com.spring.jwt.service.AttendanceService;
import com.spring.jwt.service.PayslipPdfService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendancePay")
@CrossOrigin
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    public PayslipRepository payslipRepository;
    @Autowired
    public PayslipPdfService payslipPdfService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @PostMapping
    public ResponseEntity<AttendanceDTO> create(@RequestBody AttendanceDTO dto) {
        return ResponseEntity.ok(attendanceService.createAttendance(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendance(id));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDTO>> getAll() {
        return ResponseEntity.ok(attendanceService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDTO> update(@PathVariable Long id, @RequestBody AttendanceDTO dto) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok("Attendance deleted for ID: " + id);
    }

    @GetMapping("/payslip")
    public ResponseEntity<PayslipDTO> getPayslip(
            @RequestParam Integer userId,
            @RequestParam String month,
            @RequestParam Integer year
    ) {
        Payslip payslip = payslipRepository.findByUserIdAndMonthAndYear(userId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found"));

        PayslipDTO dto = convertToDTO(payslip);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/payslip/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @RequestParam Integer userId,
            @RequestParam String month,
            @RequestParam Integer year
    ) {
        Payslip payslip = payslipRepository.findByUserIdAndMonthAndYear(userId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException("Payslip not found"));

        PayslipDTO dto = convertToDTO(payslip);
        byte[] pdf = payslipPdfService.generatePayslipPdf(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payslip_" + month + "_" + year + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/pdf")
    public ResponseEntity<?> downloadPayslipPdf(@RequestParam Integer userId,
                                                @RequestParam String month,
                                                @RequestParam Integer year) {
        try {
            Payslip payslip = payslipRepository.findByUserIdAndMonthAndYear(userId, month, year)
                    .orElseThrow(() -> new ResourceNotFoundException("Payslip not found"));

            PayslipDTO dto = convertToDTO(payslip);
            String html = generateHtmlFromTemplate(dto);
            byte[] pdfBytes = payslipPdfService.generatePayslipPdfFromHtml(html);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("Payslip_" + month + "_" + year + ".pdf").build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/login?error=" + ex.getMessage())
                    .build();
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading payslip template.");
        }
    }

    private PayslipDTO convertToDTO(Payslip payslip) {
        PayslipDTO dto = new PayslipDTO();
        BeanUtils.copyProperties(payslip, dto);
        return dto;
    }

    private String generateHtmlFromTemplate(PayslipDTO dto) throws IOException {
        String templatePath = "src/main/resources/templates/payslipTemplate.html";
        String htmlTemplate = new String(Files.readAllBytes(Paths.get(templatePath)), StandardCharsets.UTF_8);

        return htmlTemplate
                .replace("${name}", dto.getName())
                .replace("${employeeId}", dto.getEmployeeId())
                .replace("${designation}", dto.getDesignation())
                .replace("${bankName}", dto.getBankName())
                .replace("${department}", dto.getDepartment())
                .replace("${bankAccountNumber}", dto.getBankAccountNumber())
                .replace("${location}", dto.getLocation())
                .replace("${pan}", dto.getPan())
                .replace("${dateOfJoining}", dto.getDateOfJoining())
                .replace("${absentDays}", String.valueOf(dto.getAbsentDays()))
                .replace("${month}", dto.getMonth())
                .replace("${year}", String.valueOf(dto.getYear()))
                .replace("${basicPay}", String.valueOf(dto.getBasicPay()))
                .replace("${hra}", String.valueOf(dto.getHra()))
                .replace("${da}", String.valueOf(dto.getDa()))
                .replace("${travelAllowances}", String.valueOf(dto.getTravelAllowances()))
                .replace("${specialAllowances}", String.valueOf(dto.getSpecialAllowances()))
                .replace("${totalEarnings}", String.valueOf(dto.getTotalEarnings()))
                .replace("${totalDeductions}", String.valueOf(dto.getTotalDeductions()))
                .replace("${netSalary}", String.valueOf(dto.getNetSalary()))
                .replace("${workingDays}", String.valueOf(dto.getPresentDays()));
    }
}
