package com.spring.jwt.Exam.service;

import com.spring.jwt.Exam.Dto.CreateExamLinkRequest;
import com.spring.jwt.Exam.Dto.CreateExamLinkResponse;
import org.springframework.http.ResponseEntity;

public interface ExamLinkService {
    CreateExamLinkResponse createExamLink(CreateExamLinkRequest request);
    ResponseEntity<?> useExamLink(String uuid);
}
