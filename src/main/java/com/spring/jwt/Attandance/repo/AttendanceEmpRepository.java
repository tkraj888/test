package com.spring.jwt.Attandance.repo;

import com.spring.jwt.Attandance.entity.AttendanceEmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceEmpRepository extends JpaRepository<AttendanceEmp, Long> {
    /**
 * Retrieves the attendance record for a specific user on a given date.
 *
 * @param userId the unique identifier of the user
 * @param today the date for which to retrieve the attendance record
 * @return an Optional containing the attendance record if found, or empty if not present
 */
Optional<AttendanceEmp> findByUserIdAndDate(Long userId, LocalDate today);

    /**
 * Retrieves all attendance records for a specific user within the given date range, inclusive.
 *
 * @param userId the ID of the user whose attendance records are to be retrieved
 * @param startDate the start date of the range (inclusive)
 * @param endDate the end date of the range (inclusive)
 * @return a list of attendance records for the user within the specified date range
 */
List<AttendanceEmp> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);



}
