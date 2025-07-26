package com.spring.jwt.Exam.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PaperWithQuestionsDTOnn {
    private Integer sessionId;
    private Integer paperId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isLive;
    private String studentClass;
    private Integer paperPatternId;
    private List<QuestionNoAnswerDTO> questions;
}
