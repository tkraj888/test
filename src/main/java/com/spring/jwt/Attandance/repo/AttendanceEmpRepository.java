package com.spring.jwt.Attandance.repo;

import com.spring.jwt.Attandance.entity.AttendanceEmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceEmpRepository extends JpaRepository<AttendanceEmp, Long> {
    Optional<AttendanceEmp> findByUserIdAndDate(Long userId, LocalDate today);

    List<AttendanceEmp> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);



}
