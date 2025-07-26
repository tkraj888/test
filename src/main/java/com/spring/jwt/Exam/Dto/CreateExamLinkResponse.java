package com.spring.jwt.Exam.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateExamLinkResponse {
    private String uuid;
    private String examUrl;
}
