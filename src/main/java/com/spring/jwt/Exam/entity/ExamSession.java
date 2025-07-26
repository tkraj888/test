package com.spring.jwt.Exam.entity;


import com.spring.jwt.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "paper_id")
    private Paper paper;
    private String StudentClass;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime resultDate;
    private Double score;
    private Double negativeCount;
    private Double negativeScore;

    @OneToMany(mappedBy = "examSession", cascade = CascadeType.ALL)
    private List<UserAnswer> userAnswers;
}