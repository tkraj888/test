package com.spring.jwt.Attandance.repo;

import com.spring.jwt.Attandance.entity.AttendanceEmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceEmpRepository extends JpaRepository<AttendanceEmp, Long> {
    /****
 * Retrieves the attendance record for the specified user on the given date.
 *
 * @param userId the unique identifier of the user
 * @param today the date for which to retrieve the attendance record
 * @return an Optional containing the attendance record if it exists, or empty if not found
 */
Optional<AttendanceEmp> findByUserIdAndDate(Long userId, LocalDate today);

    /**
 * Returns all attendance records for a user within the specified inclusive date range.
 *
 * @param userId the ID of the user
 * @param startDate the start date of the range (inclusive)
 * @param endDate the end date of the range (inclusive)
 * @return a list of attendance records for the user between the given dates
 */
List<AttendanceEmp> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);



}
