package com.spring.jwt.Exam.controller;

import com.spring.jwt.Exam.Dto.CreateExamLinkRequest;
import com.spring.jwt.Exam.Dto.CreateExamLinkResponse;
import com.spring.jwt.Exam.Dto.PaperWithQuestionsDTOn;
import com.spring.jwt.Exam.service.ExamLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class ExamLinkController {

    private final ExamLinkService examLinkService;

    @PostMapping("/create")

    public ResponseEntity<CreateExamLinkResponse> createExamLink(@RequestBody CreateExamLinkRequest request) {
        System.out.println("11");
        return ResponseEntity.ok(examLinkService.createExamLink(request));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> useExamLink(@PathVariable String uuid) {
        try {
            return examLinkService.useExamLink(uuid);
        } catch (Exception e) {
            e.printStackTrace(); // or use logger.error()
            return ResponseEntity.status(500).body("Internal error: " + e.getMessage());
        }
    }


}
