package com.spring.jwt.Attandance.Service;

import com.spring.jwt.Attandance.Dto.AttendanceEmpCreateDto;
import com.spring.jwt.Attandance.Dto.AttendanceEmpDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceEmpService {
    /**
 * Saves a new employee attendance record with the specified type.
 *
 * @param dto  the attendance data to be saved
 * @param type the type of attendance (e.g., present, absent)
 * @return the created attendance record
 */
AttendanceEmpCreateDto saveAttendance(AttendanceEmpCreateDto dto, String type);
    /**
 * Retrieves the attendance record corresponding to the specified attendance ID.
 *
 * @param id the unique identifier of the attendance record
 * @return the attendance record details, or null if not found
 */
AttendanceEmpDto getAttendanceById(Long id);
    /**
 * Retrieves all employee attendance records.
 *
 * @return a list of all attendance records as AttendanceEmpDto objects
 */
List<AttendanceEmpDto> getAllAttendance();
    /**
 * Updates an existing attendance record identified by its ID with new data.
 *
 * @param id  the unique identifier of the attendance record to update
 * @param dto the data transfer object containing updated attendance information
 * @return the updated attendance record as a data transfer object
 */
AttendanceEmpDto updateAttendance(Long id, AttendanceEmpDto dto);

    /**
 * Retrieves attendance records for a specific user within the specified date range.
 *
 * @param userId the unique identifier of the user
 * @param startDate the start date of the range (inclusive)
 * @param endDate the end date of the range (inclusive)
 * @return a list of attendance records matching the user and date range
 */
List<AttendanceEmpDto> getAttendanceByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
 * Retrieves attendance records for a specific user filtered by month and year.
 *
 * @param userId the unique identifier of the user
 * @param month the month for which attendance records are requested (1-12)
 * @param year the year for which attendance records are requested
 * @return a list of attendance records for the specified user, month, and year
 */
List<AttendanceEmpDto> getAttendanceByUserIdAndMonth(Long userId, int month, int year);

}
