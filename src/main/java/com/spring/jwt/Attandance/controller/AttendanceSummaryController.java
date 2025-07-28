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
     * Saves a new attendance summary and returns the saved summary.
     *
     * @param dto the attendance summary data to be saved
     * @return the saved attendance summary wrapped in a ResponseEntity
     */
    @PostMapping
    public ResponseEntity<AttendanceSummaryDto> save(@RequestBody AttendanceSummaryDto dto) {
        AttendanceSummaryDto saved = attendanceSummaryService.saveSummary(dto);
        return ResponseEntity.ok(saved);
    }

    /**
     * Retrieves an attendance summary by its unique identifier.
     *
     * @param id the unique identifier of the attendance summary
     * @return a ResponseEntity containing the requested AttendanceSummaryDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceSummaryDto> getById(@PathVariable Long id) {
        AttendanceSummaryDto dto = attendanceSummaryService.getSummaryById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all attendance summaries.
     *
     * @return a ResponseEntity containing a list of all AttendanceSummaryDto objects
     */
    @GetMapping
    public ResponseEntity<List<AttendanceSummaryDto>> getAll() {
        List<AttendanceSummaryDto> list = attendanceSummaryService.getAllSummaries();
        return ResponseEntity.ok(list);
    }

    /****
     * Updates an existing attendance summary identified by its ID.
     *
     * @param id the ID of the attendance summary to update
     * @param dto the updated attendance summary data
     * @return the updated attendance summary wrapped in an HTTP 200 OK response
     */
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceSummaryDto> update(
            @PathVariable Long id,
            @RequestBody AttendanceSummaryDto dto
    ) {
        AttendanceSummaryDto updated = attendanceSummaryService.updateSummary(id, dto);
        return ResponseEntity.ok(updated);
    }

    /****
     * Retrieves the monthly attendance report for a specific user and month.
     *
     * @param userId the ID of the user whose attendance report is requested
     * @param month the month for which the report is generated, formatted as "yyyy-MM"
     * @return the monthly attendance report for the specified user and month
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
     * Handles ResourceNotFoundException by returning a 404 Not Found response with the exception message as the response body.
     *
     * @param ex the ResourceNotFoundException thrown during request processing
     * @return a ResponseEntity containing the exception message and HTTP 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Handles uncaught exceptions by returning a 500 Internal Server Error response with a generic error message.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing the error message and HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(500).body("Something went wrong: " + ex.getMessage());
    }
}
