package com.spring.jwt.Attandance.Service;

import com.spring.jwt.Attandance.Dto.AttendanceEmpCreateDto;
import com.spring.jwt.Attandance.Dto.AttendanceEmpDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceEmpService {
    AttendanceEmpCreateDto saveAttendance(AttendanceEmpCreateDto dto, String type);
    AttendanceEmpDto getAttendanceById(Long id);
    List<AttendanceEmpDto> getAllAttendance();
    AttendanceEmpDto updateAttendance(Long id, AttendanceEmpDto dto);

    List<AttendanceEmpDto> getAttendanceByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    List<AttendanceEmpDto> getAttendanceByUserIdAndMonth(Long userId, int month, int year);

}
