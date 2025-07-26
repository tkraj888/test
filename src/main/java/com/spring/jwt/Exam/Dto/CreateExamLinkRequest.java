package com.spring.jwt.Exam.Dto;

import lombok.Data;

@Data
public class CreateExamLinkRequest {
    private Integer paperId;
    private Long userId;
    private String studentClass;
}
