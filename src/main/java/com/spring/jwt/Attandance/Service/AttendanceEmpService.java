package com.spring.jwt.Attandance.Service;

import com.spring.jwt.Attandance.Dto.AttendanceEmpCreateDto;
import com.spring.jwt.Attandance.Dto.AttendanceEmpDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceEmpService {
    /**
 * Creates and saves a new employee attendance record with the specified attendance type.
 *
 * @param dto  the data for the new attendance record
 * @param type the attendance type to assign (e.g., present, absent)
 * @return the newly created attendance record
 */
AttendanceEmpCreateDto saveAttendance(AttendanceEmpCreateDto dto, String type);
    /**
 * Retrieves the attendance record for the given unique attendance ID.
 *
 * @param id the unique identifier of the attendance record
 * @return the attendance record details as an AttendanceEmpDto, or null if no record exists for the specified ID
 */
AttendanceEmpDto getAttendanceById(Long id);
    /**
 * Returns a list of all employee attendance records.
 *
 * @return a list containing all attendance records as AttendanceEmpDto objects
 */
List<AttendanceEmpDto> getAllAttendance();
    /**
 * Updates an employee attendance record with new information.
 *
 * @param id the unique identifier of the attendance record to update
 * @param dto the updated attendance data
 * @return the updated attendance record, or null if the record does not exist
 */
AttendanceEmpDto updateAttendance(Long id, AttendanceEmpDto dto);

    /**
 * Returns attendance records for a given user within a specified inclusive date range.
 *
 * @param userId    the unique identifier of the user
 * @param startDate the start date of the range (inclusive)
 * @param endDate   the end date of the range (inclusive)
 * @return a list of attendance records for the user within the date range
 */
List<AttendanceEmpDto> getAttendanceByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
 * Returns attendance records for a user within a specified month and year.
 *
 * @param userId the unique identifier of the user
 * @param month the month (1-12) to filter attendance records
 * @param year the year to filter attendance records
 * @return a list of attendance records matching the user, month, and year
 */
List<AttendanceEmpDto> getAttendanceByUserIdAndMonth(Long userId, int month, int year);

}
