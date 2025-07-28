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

    // Save login or logout attendance
    @PostMapping("/mark")
    public ResponseEntity<AttendanceEmpCreateDto> markAttendance(
            @RequestBody AttendanceEmpCreateDto dto,
            @RequestParam String type // login or logout
    ) {
        AttendanceEmpCreateDto attendanceEmpCreateDto = attendanceService.saveAttendance(dto, type);
        return ResponseEntity.ok(attendanceEmpCreateDto);
    }

    // Get attendance by ID
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceEmpDto> getById(@PathVariable Long id) {
        AttendanceEmpDto dto = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(dto);
    }

    // Get all attendance records
    @GetMapping
    public ResponseEntity<List<AttendanceEmpDto>> getAll() {
        List<AttendanceEmpDto> list = attendanceService.getAllAttendance();
        return ResponseEntity.ok(list);
    }

    // Update attendance by ID
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceEmpDto> updateAttendance(
            @PathVariable Long id,
            @RequestBody AttendanceEmpDto dto
    ) {
        AttendanceEmpDto updated = attendanceService.updateAttendance(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Get attendance by userId and date range
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<AttendanceEmpDto>> getByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<AttendanceEmpDto> list = attendanceService.getAttendanceByUserIdAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(list);
    }

    // Get attendance by userId and month
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
