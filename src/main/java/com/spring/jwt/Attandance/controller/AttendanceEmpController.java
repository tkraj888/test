package com.spring.jwt.Attandance.controller;

import com.spring.jwt.Attandance.Dto.AttendanceEmpCreateDto;
import com.spring.jwt.Attandance.Dto.AttendanceEmpDto;
import com.spring.jwt.Attandance.Service.AttendanceEmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceEmpController {

    private final AttendanceEmpService attendanceService;

    /**
     * Marks an employee's attendance as either login or logout.
     *
     * @param dto the attendance details to record
     * @param type indicates whether the entry is for "login" or "logout"
     * @return a ResponseEntity containing the saved attendance entry
     */
    @PostMapping("/mark")
    public ResponseEntity<AttendanceEmpCreateDto> markAttendance(
            @RequestBody AttendanceEmpCreateDto dto,
            @RequestParam String type // login or logout
    ) {
        AttendanceEmpCreateDto attendanceEmpCreateDto = attendanceService.saveAttendance(dto, type);
        return ResponseEntity.ok(attendanceEmpCreateDto);
    }

    /**
     * Retrieves a specific employee attendance record by its unique identifier.
     *
     * @param id the unique identifier of the attendance record
     * @return a ResponseEntity containing the corresponding AttendanceEmpDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceEmpDto> getById(@PathVariable Long id) {
        AttendanceEmpDto dto = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Returns all employee attendance records.
     *
     * @return a ResponseEntity containing a list of all attendance records as AttendanceEmpDto objects
     */
    @GetMapping
    public ResponseEntity<List<AttendanceEmpDto>> getAll() {
        List<AttendanceEmpDto> list = attendanceService.getAllAttendance();
        return ResponseEntity.ok(list);
    }

    /**
     * Updates an employee attendance record identified by its ID with new data.
     *
     * @param id the unique identifier of the attendance record to update
     * @param dto the new attendance data to apply
     * @return a ResponseEntity containing the updated attendance record
     */
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceEmpDto> updateAttendance(
            @PathVariable Long id,
            @RequestBody AttendanceEmpDto dto
    ) {
        AttendanceEmpDto updated = attendanceService.updateAttendance(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Returns attendance records for a specific user within the specified inclusive date range.
     *
     * @param userId the unique identifier of the user
     * @param startDate the start date of the range (inclusive)
     * @param endDate the end date of the range (inclusive)
     * @return a ResponseEntity containing a list of attendance records for the user in the given date range
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<AttendanceEmpDto>> getByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<AttendanceEmpDto> list = attendanceService.getAttendanceByUserIdAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(list);
    }

    /**
     * Returns attendance records for a specific user filtered by the given month and year.
     *
     * @param userId the unique identifier of the user
     * @param month the month (1-12) to filter attendance records
     * @param year the year to filter attendance records
     * @return a ResponseEntity containing a list of attendance records for the specified user, month, and year
     */
    @GetMapping("/user/{userId}/month")
    public ResponseEntity<List<AttendanceEmpDto>> getByMonth(
            @PathVariable Long userId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        List<AttendanceEmpDto> list = attendanceService.getAttendanceByUserIdAndMonth(userId, month, year);
        return ResponseEntity.ok(list);
    }
}
