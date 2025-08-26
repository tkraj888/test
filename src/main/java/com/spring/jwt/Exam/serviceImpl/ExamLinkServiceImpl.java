package com.spring.jwt.Exam.serviceImpl;


import com.spring.jwt.Exam.Dto.*;
import com.spring.jwt.Exam.entity.ExamAccessLink;
import com.spring.jwt.Exam.entity.ExamSession;
import com.spring.jwt.Exam.entity.Paper;
import com.spring.jwt.Exam.repository.ExamAccessLinkRepository;
import com.spring.jwt.Exam.repository.ExamSessionRepository;
import com.spring.jwt.Exam.repository.PaperRepository;
import com.spring.jwt.Exam.service.ExamLinkService;
import com.spring.jwt.entity.Question;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamLinkServiceImpl implements ExamLinkService {

    private final ExamAccessLinkRepository examAccessLinkRepository;
    private final PaperRepository paperRepository;
    private final UserRepository userRepository;
    private final ExamSessionRepository examSessionRepository;

    @Override
    public CreateExamLinkResponse createExamLink(CreateExamLinkRequest request) {
        Paper paper = paperRepository.findById(request.getPaperId())
                .orElseThrow(() -> new ResourceNotFoundException("Paper not found"));

        String uuid = UUID.randomUUID().toString();

        ExamAccessLink link = ExamAccessLink.builder()
                .uuid(uuid)
                .paper(paper)
                .userId(request.getUserId())
                .studentClass(request.getStudentClass())
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        examAccessLinkRepository.save(link);

        return CreateExamLinkResponse.builder()
                .uuid(uuid)
                .examUrl("https://gtasterixt.netlify.app/test/" + uuid)
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> useExamLink(String uuid) {
        try {
            ExamAccessLink link = examAccessLinkRepository.findById(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired exam link"));

            if (link.isUsed()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Test already done");
            }

            Paper paper = link.getPaper();
            if (paper == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Paper not associated with this link.");
            }

            LocalDateTime now = LocalDateTime.now();
            if ((paper.getStartTime() != null && now.isBefore(paper.getStartTime())) ||
                    (paper.getEndTime() != null && now.isAfter(paper.getEndTime()))) {
                return ResponseEntity.status(HttpStatus.GONE)
                        .body("Exam not accessible at this time.");
            }

            // Mark as used
            link.setUsed(true);
            examAccessLinkRepository.save(link);

            // Create Exam Session
            User user = userRepository.findById(link.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            ExamSession session = new ExamSession();
            session.setUser(user);
            session.setPaper(paper);
            session.setStudentClass(link.getStudentClass());
            session.setStartTime(now);
            session.setScore(0.0);
            session.setUserAnswers(new ArrayList<>());

            examSessionRepository.save(session);

            PaperWithQuestionsDTOn dto = buildExamDTO(session);
            return ResponseEntity.ok(dto);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Logs to console or server log
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }


    private PaperWithQuestionsDTOn buildExamDTO(ExamSession session) {
        Paper paper = session.getPaper();

        List<QuestionNoAnswerDTO> questionDTOs = paper.getPaperQuestions().stream()
                .map(pq -> convertToQuestionNoAnswerDTO(pq.getQuestion()))
                .collect(Collectors.toList());

//        Collections.shuffle(questionDTOs);

        return PaperWithQuestionsDTOn.builder()
                .sessionId(session.getSessionId())
                .paperId(paper.getPaperId())
                .title(paper.getTitle())
                .description(paper.getDescription())
                .startTime(paper.getStartTime())
                .endTime(paper.getEndTime())
                .isLive(paper.getIsLive())
                .studentClass(session.getStudentClass())
                .paperPatternId(paper.getPaperPattern() != null ? paper.getPaperPattern().getPaperPatternId() : null)
                .questions(questionDTOs)
                .build();
    }

    private QuestionNoAnswerDTO convertToQuestionNoAnswerDTO(Question question) {
        return QuestionNoAnswerDTO.builder()
                .questionId(Long.valueOf(question.getQuestionId()))
                .questionText(question.getQuestionText())
                .type((question.getType()))
                .subject(question.getSubject())
                .level(question.getLevel())
                .marks(Double.valueOf(question.getMarks()))
                .userId(Long.valueOf(question.getUserId()))
                .option1(question.getOption1())
                .option2(question.getOption2())
                .option3(question.getOption3())
                .option4(question.getOption4())
                .studentClass(question.getStudentClass())
                .isDescriptive(question.isDescriptive())
                .isMultiOptions(question.isMultiOptions())
                .build();
    }


}
