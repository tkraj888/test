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
     * Records a login or logout attendance entry for an employee.
     *
     * @param dto the attendance data to be saved
     * @param type specifies whether the attendance is for "login" or "logout"
     * @return the saved attendance entry wrapped in a ResponseEntity
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
     * Retrieves an employee attendance record by its unique ID.
     *
     * @param id the unique identifier of the attendance record
     * @return a ResponseEntity containing the attendance record DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceEmpDto> getById(@PathVariable Long id) {
        AttendanceEmpDto dto = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all employee attendance records.
     *
     * @return a ResponseEntity containing a list of all AttendanceEmpDto objects
     */
    @GetMapping
    public ResponseEntity<List<AttendanceEmpDto>> getAll() {
        List<AttendanceEmpDto> list = attendanceService.getAllAttendance();
        return ResponseEntity.ok(list);
    }

    /**
     * Updates an existing attendance record by its ID.
     *
     * @param id the ID of the attendance record to update
     * @param dto the updated attendance data
     * @return the updated attendance record wrapped in a ResponseEntity
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
     * Retrieves attendance records for a specific user within a given date range.
     *
     * @param userId     the ID of the user whose attendance records are requested
     * @param startDate  the start date of the range (inclusive), in ISO format
     * @param endDate    the end date of the range (inclusive), in ISO format
     * @return a ResponseEntity containing a list of attendance records for the user within the specified date range
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
     * Retrieves attendance records for a specific user filtered by month and year.
     *
     * @param userId the ID of the user whose attendance records are to be retrieved
     * @param month the month (1-12) for which attendance records are requested
     * @param year the year for which attendance records are requested
     * @return a ResponseEntity containing a list of AttendanceEmpDto objects for the specified user, month, and year
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
