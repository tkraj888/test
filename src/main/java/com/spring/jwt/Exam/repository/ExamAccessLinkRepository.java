package com.spring.jwt.Exam.repository;

import com.spring.jwt.Exam.entity.ExamAccessLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamAccessLinkRepository extends JpaRepository<ExamAccessLink, String> {
}
