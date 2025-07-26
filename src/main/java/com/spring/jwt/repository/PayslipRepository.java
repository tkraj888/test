package com.spring.jwt.repository;

import com.spring.jwt.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    Optional<Payslip> findByUserIdAndMonthAndYear(Integer userId, String month, Integer year);
}
