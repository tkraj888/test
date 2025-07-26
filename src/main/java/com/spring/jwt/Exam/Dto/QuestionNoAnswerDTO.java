package com.spring.jwt.Exam.Dto;
import com.spring.jwt.entity.enum01.QType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionNoAnswerDTO {
    private Long questionId;
    private String questionText;
    private QType type;
    private String subject;
    private String level;
    private Double marks;
    private Long userId;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String studentClass;
    private boolean isDescriptive;
    private boolean isMultiOptions;
}
