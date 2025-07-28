package com.spring.jwt.Attandance.repo;

import com.spring.jwt.Attandance.entity.AttendanceSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface AttendanceSummaryRepository extends JpaRepository<AttendanceSummary, Long> {
    /**
 * Retrieves an attendance summary for a specific user on a given date.
 *
 * @param userId the ID of the user whose attendance summary is to be retrieved
 * @param date the date for which the attendance summary is requested
 * @return an {@code Optional} containing the attendance summary if found, or empty if not present
 */
Optional<AttendanceSummary> findByUserIdAndDate(Long userId, LocalDate date);

    List<AttendanceSummary> findByUserId(Long userId);

}
