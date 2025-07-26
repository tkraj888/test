package com.spring.jwt.service;

import com.spring.jwt.dto.AttendanceDTO;

import java.util.List;

public interface AttendanceService {
    AttendanceDTO createAttendance(AttendanceDTO dto);
    AttendanceDTO getAttendance(Long id);
    List<AttendanceDTO> getAll();
    AttendanceDTO updateAttendance(Long id, AttendanceDTO dto);
    void deleteAttendance(Long id);
}
