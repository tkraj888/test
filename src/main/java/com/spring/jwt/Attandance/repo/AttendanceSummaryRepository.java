package com.spring.jwt.Attandance.repo;

import com.spring.jwt.Attandance.entity.AttendanceSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceSummaryRepository extends JpaRepository<AttendanceSummary, Long> {
    Optional<AttendanceSummary> findByUserIdAndDate(Long userId, LocalDate date);
}
