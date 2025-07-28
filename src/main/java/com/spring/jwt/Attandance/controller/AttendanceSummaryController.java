package com.spring.jwt.Attandance.controller;

import com.spring.jwt.Attandance.Dto.AttendanceSummaryDto;
import com.spring.jwt.Attandance.Dto.MonthlyAttendanceReportDto;
import com.spring.jwt.Attandance.Service.AttendanceSummaryService;
import com.spring.jwt.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendanceSummary")
@RequiredArgsConstructor
public class AttendanceSummaryController {

    private final AttendanceSummaryService attendanceSummaryService;

    /**
     * Save a new attendance summary
     */
    @PostMapping
    public ResponseEntity<AttendanceSummaryDto> save(@RequestBody AttendanceSummaryDto dto) {
        AttendanceSummaryDto saved = attendanceSummaryService.saveSummary(dto);
        return ResponseEntity.ok(saved);
    }

    /**
     * Get attendance summary by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceSummaryDto> getById(@PathVariable Long id) {
        AttendanceSummaryDto dto = attendanceSummaryService.getSummaryById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Get all attendance summaries
     */
    @GetMapping
    public ResponseEntity<List<AttendanceSummaryDto>> getAll() {
        List<AttendanceSummaryDto> list = attendanceSummaryService.getAllSummaries();
        return ResponseEntity.ok(list);
    }

    /**
     * Update an existing attendance summary by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceSummaryDto> update(
            @PathVariable Long id,
            @RequestBody AttendanceSummaryDto dto
    ) {
        AttendanceSummaryDto updated = attendanceSummaryService.updateSummary(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Get monthly attendance report for a specific user
     * Example: /api/attendance-summary/monthly?userId=1&month=2025-07
     */
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyAttendanceReportDto> getMonthlyReport(
            @RequestParam Long userId,
            @RequestParam String month
    ) {
        MonthlyAttendanceReportDto report = attendanceSummaryService.getMonthlyAttendanceReport(userId, month);
        return ResponseEntity.ok(report);
    }

    /**
     * Handle ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Handle all other exceptions (optional)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(500).body("Something went wrong: " + ex.getMessage());
    }
}
