package com.spring.jwt.Exam.serviceImpl;

import com.spring.jwt.Exam.Dto.*;
import com.spring.jwt.Exam.entity.NegativeMarks;
import com.spring.jwt.Exam.entity.Paper;
import com.spring.jwt.Exam.entity.PaperQuestion;
import com.spring.jwt.Exam.repository.PaperRepository;
import com.spring.jwt.Exam.service.PaperService;
import com.spring.jwt.PaperPattern.PaperPatternRepository;
import com.spring.jwt.Question.QuestionRepository;
import com.spring.jwt.dto.PageResponseDto;
import com.spring.jwt.entity.PaperPattern;
import com.spring.jwt.entity.Question;
import com.spring.jwt.Question.QuestionDTO;
import com.spring.jwt.entity.enum01.EventType;
import com.spring.jwt.exception.InvalidPaginationParameterException;
import com.spring.jwt.exception.PaperFetchException;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PaperPatternRepository paperPatternRepository;


    private PaperDTO toDTO(Paper entity) {
        if (entity == null) return null;

        PaperDTO dto = new PaperDTO();
        dto.setPaperId(entity.getPaperId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setIsLive(entity.getIsLive());
        dto.setStudentClass(entity.getStudentClass());
        dto.setResultDate(entity.getResultDate());

        // Safely set paperPatternId
        if (entity.getPaperPattern() != null) {
            dto.setPaperPatternId(entity.getPaperPattern().getPaperPatternId());
            dto.setPatternName(entity.getPaperPattern().getPatternName());
        }

        // Set Question IDs
        if (entity.getPaperQuestions() != null) {
            dto.setQuestions(
                    entity.getPaperQuestions().stream()
                            .map(pq -> pq.getQuestion() != null ? pq.getQuestion().getQuestionId() : null)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private PaperDTO1 toDTO2(Paper entity) {
        if (entity == null) return null;

        PaperDTO1 dto = new PaperDTO1();
        dto.setPaperId(entity.getPaperId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setIsLive(entity.getIsLive());
        dto.setStudentClass(entity.getStudentClass());

        // Paper pattern info
        if (entity.getPaperPattern() != null) {
            dto.setPaperPatternId(entity.getPaperPattern().getPaperPatternId());
            dto.setPatternName(entity.getPaperPattern().getPatternName());
        }

        dto.setResultDate(entity.getResultDate());

        // Map question IDs
        if (entity.getPaperQuestions() != null) {
            dto.setQuestions(
                    entity.getPaperQuestions().stream()
                            .map(pq -> pq.getQuestion() != null ? pq.getQuestion().getQuestionId() : null)
                            .collect(Collectors.toList())
            );
        }

        // Map negative marks
        if (entity.getNegativeMarksList() != null && !entity.getNegativeMarksList().isEmpty()) {
            dto.setNegativeMarksList(
                    entity.getNegativeMarksList().stream()
                            .map(nm -> NegativeMarksDTO.builder()
                                    .questionId(nm.getQuestionId())
                                    .negativeMark(nm.getNegativeMark())
                                    .build())
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private Paper toEntity(PaperDTO dto) {
        if (dto == null) return null;

        Paper entity = new Paper();
        entity.setPaperId(dto.getPaperId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setIsLive(dto.getIsLive());
        entity.setStudentClass(dto.getStudentClass());
        entity.setResultDate(dto.getResultDate());


        // Set PaperPattern from ID
        if (dto.getPaperPatternId() != null) {
            PaperPattern pattern = paperPatternRepository.findById(dto.getPaperPatternId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paper pattern not found with id: " + dto.getPaperPatternId()));
            entity.setPaperPattern(pattern);
        }

        // Set Questions
        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            List<PaperQuestion> paperQuestions = dto.getQuestions().stream().map(qId -> {
                PaperQuestion pq = new PaperQuestion();
                pq.setPaper(entity);
                pq.setQuestion(
                        questionRepository.findById(qId)
                                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + qId))
                );
                return pq;
            }).collect(Collectors.toList());
            entity.setPaperQuestions(paperQuestions);
        }
        // Set Negative Marks
        if (dto.getNegativeMarksList() != null && !dto.getNegativeMarksList().isEmpty()) {
            List<NegativeMarks> negativeMarksList = dto.getNegativeMarksList().stream().map(nmDto -> {
                NegativeMarks nm = new NegativeMarks();
                nm.setQuestionId(nmDto.getQuestionId());
                nm.setNegativeMark(nmDto.getNegativeMark());
                nm.setPaper(entity); // Link back to Paper
                return nm;
            }).collect(Collectors.toList());
            entity.setNegativeMarksList(negativeMarksList);
        }

        return entity;
    }


    private QuestionDTO toQuestionDTO(PaperQuestion pq) {
        if (pq == null || pq.getQuestion() == null) return null;
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestionId(pq.getQuestion().getQuestionId());
        dto.setQuestionText(pq.getQuestion().getQuestionText());
        dto.setType(pq.getQuestion().getType());
        dto.setSubject(pq.getQuestion().getSubject());
        dto.setLevel(pq.getQuestion().getLevel());
        dto.setMarks(pq.getQuestion().getMarks());
        dto.setUserId(pq.getQuestion().getUserId());
        dto.setOption1(pq.getQuestion().getOption1());
        dto.setOption2(pq.getQuestion().getOption2());
        dto.setOption3(pq.getQuestion().getOption3());
        dto.setOption4(pq.getQuestion().getOption4());
        dto.setStudentClass(pq.getQuestion().getStudentClass());
        dto.setAnswer(pq.getQuestion().getAnswer());
        return dto;
    }
    // Entity to PaperWithQuestionsDTO
    private PaperWithQuestionsDTO toDTO01(Paper entity) {
        if (entity == null) return null;
        PaperWithQuestionsDTO dto = new PaperWithQuestionsDTO();
        dto.setPaperId(entity.getPaperId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setIsLive(entity.getIsLive());
        dto.setStudentClass(entity.getStudentClass());
        if (entity.getPaperQuestions() != null) {
            dto.setQuestions(entity.getPaperQuestions().stream()
                    .map(pq -> toQuestionNoAnswerDTO(pq.getQuestion()))
                    .collect(Collectors.toList())
            );
        }
        return dto;
    }

    // Helper: Question -> QuestionNoAnswerDTO
    private QuestionNoAnswerDTO toQuestionNoAnswerDTO(Question q) {
        if (q == null) return null;
        QuestionNoAnswerDTO dto = new QuestionNoAnswerDTO();
        dto.setQuestionId(Long.valueOf(q.getQuestionId()));
        dto.setQuestionText(q.getQuestionText());
        dto.setType((q.getType()));
        dto.setSubject(q.getSubject());
        dto.setLevel(q.getLevel());
        dto.setMarks(Double.valueOf(q.getMarks()));
        dto.setUserId(Long.valueOf(q.getUserId()));
        dto.setOption1(q.getOption1());
        dto.setOption2(q.getOption2());
        dto.setOption3(q.getOption3());
        dto.setOption4(q.getOption4());
        dto.setStudentClass(q.getStudentClass());
        return dto;
    }

    public PaperWithQuestionsWithAnsDTO toPaperWithQuestionsDTO(Paper paper, List<QuestionDTO> questionDTOs) {
        return PaperWithQuestionsWithAnsDTO.builder()
                .paperId(paper.getPaperId())
                .title(paper.getTitle())
                .description(paper.getDescription())
                .startTime(paper.getStartTime())
                .endTime(paper.getEndTime())
                .isLive(paper.getIsLive())
                .studentClass(paper.getStudentClass())
                .questions(questionDTOs)
                .build();
    }


    @Override
    public PaperDTO createPaper(PaperDTO paperDTO) {
        // 1. Fetch PaperPattern
        Integer patternId = paperDTO.getPaperPatternId();
        PaperPattern pattern = paperPatternRepository.findById(patternId)
                .orElseThrow(() -> new PaperFetchException("Invalid PaperPattern ID: " + patternId));

        // 2. Validate question count
        int requiredQuestions = pattern.getNoOfQuestion();
        List<Integer> questionIds = paperDTO.getQuestions();
        if (questionIds == null || questionIds.size() != requiredQuestions) {
            throw new PaperFetchException(
                    "Number of questions (" + (questionIds == null ? 0 : questionIds.size()) +
                            ") does not match the required (" + requiredQuestions + ") by the pattern."
            );
        }

        // 3. Validate sum of marks
        int totalMarks = 0;
        for (Integer qId : questionIds) {
            Question question = questionRepository.findById(qId)
                    .orElseThrow(() -> new PaperFetchException("Invalid Question ID: " + qId));
            totalMarks += question.getMarks(); // assuming getMarks() returns int
        }
        if (totalMarks != pattern.getMarks()) {
            throw new IllegalArgumentException(
                    "Sum of question marks (" + totalMarks + ") does not match pattern marks (" + pattern.getMarks() + ")."
            );
        }
        // 3.5 Validate Negative Marks only for valid questionIds
        if (paperDTO.getNegativeMarksList() != null) {
            for (NegativeMarksDTO nmDto : paperDTO.getNegativeMarksList()) {
                if (!questionIds.contains(nmDto.getQuestionId())) {
                    throw new PaperFetchException("Negative mark set for invalid question ID: " + nmDto.getQuestionId());
                }
                if (nmDto.getNegativeMark() < 0) {
                    throw new PaperFetchException("Negative mark value must not be negative: " + nmDto.getNegativeMark());
                }
            }
        }

        // 4. Proceed with saving
        Paper paper = toEntity(paperDTO);
        paper.setPaperPattern(pattern);
        Paper saved = paperRepository.save(paper);


        return toDTO(saved);
    }

    @Override
    public PaperDTO1 getPaper(Integer id) {
        Paper paper = paperRepository.findById(id)
                .orElseThrow(() -> new PaperFetchException("Paper not found with id: " + id));
        return toDTO2(paper);
    }

    @Override
    public PageResponseDto<PaperDTO> getAllPapers(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new InvalidPaginationParameterException("Page number must be >= 0 and size > 0");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("paperId").descending());
        Page<Paper> paperPage = paperRepository.findAll(pageable);

        List<PaperDTO> paperDTOs = paperPage.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                paperDTOs,
                paperPage.getNumber(),
                paperPage.getSize(),
                paperPage.getTotalElements(),
                paperPage.getTotalPages()
        );
    }


    @Override
    public PaperDTO updatePaper(Integer id, PaperDTO paperDTO) {
        Paper paper = paperRepository.findById(id)
                .orElseThrow(() -> new PaperFetchException("Paper not found with id: " + id));
        paper.setTitle(paperDTO.getTitle());
        paper.setDescription(paperDTO.getDescription());
        paper.setStartTime(paperDTO.getStartTime());
        paper.setEndTime(paperDTO.getEndTime());
        paper.setIsLive(paperDTO.getIsLive());
        paper.setStudentClass(paperDTO.getStudentClass());
        // PaperQuestions update logic can be added if needed
        Paper saved = paperRepository.save(paper);
        return toDTO(saved);
    }

    @Override
    public void deletePaper(Integer id) {
        Paper paper = paperRepository.findById(id)
                .orElseThrow(() -> new PaperFetchException("Paper not found with id: " + id));
        paperRepository.delete(paper);
    }

    @Override
    public PaperWithQuestionsDTO getPaperWithQuestions(Integer paperId) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new PaperFetchException("Paper not found with id: " + paperId));
        return toDTO01(paper);
    }


    @Override
    public List<PaperDTO> getLivePapers(String studentClass) {
        try {
            List<Paper> livePapers = paperRepository.findByIsLiveTrueAndStudentClass(studentClass);
            if (livePapers == null || livePapers.isEmpty()) {
                throw new PaperFetchException("No live papers found for studentClass: " + studentClass);
            }
            return livePapers.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch live papers.", e);
        }
    }

    /**
     * Fetch paper with all associated questions including answers and solutions.
     */
    @Override
    public PaperWithQuestionsAndAnswersDTO getPaperWithSolutions(Integer paperId) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new ResourceNotFoundException("Paper not found with ID: " + paperId));

        List<Question> questions = questionRepository.findQuestionsByPaperId(paperId);

        if (questions == null || questions.isEmpty()) {
            System.out.println("No questions found for paperId: " + paperId);
        } else {
            System.out.println("Questions found: " + questions.size());
        }

        List<QuestionWithAnswerDTO> questionDTOs = questions.stream()
                .map(this::mapToQuestionWithAnswerDTO)
                .collect(Collectors.toList());

        return PaperWithQuestionsAndAnswersDTO.builder()
                .paperId(paper.getPaperId())
                .title(paper.getTitle())
                .description(paper.getDescription())
                .startTime(paper.getStartTime())
                .endTime(paper.getEndTime())
                .isLive(paper.getIsLive())
                .studentClass(paper.getStudentClass())
                .paperPatternId(paper.getPaperPattern() != null ? paper.getPaperPattern().getPaperPatternId() : null)
                .questions(questionDTOs)
                .build();
    }




    /**
     * Maps a Question entity to a DTO including answer and hint.
     */
    private QuestionWithAnswerDTO mapToQuestionWithAnswerDTO(Question question) {
        if (question == null) {
            return null;
        }

        return QuestionWithAnswerDTO.builder()
                .questionId(question.getQuestionId())
                .questionText(question.getQuestionText())
                .type(question.getType())
                .subject(question.getSubject())
                .unit(question.getUnit())
                .chapter(question.getChapter())
                .topic(question.getTopic())
                .level(question.getLevel())
                .marks(question.getMarks())
                .userId(question.getUserId())
                .option1(question.getOption1())
                .option2(question.getOption2())
                .option3(question.getOption3())
                .option4(question.getOption4())
                .studentClass(question.getStudentClass())
                .isDescriptive(question.isDescriptive())
                .isMultiOptions(question.isMultiOptions())
                .answer(question.getAnswer())
                .hintAndSol(question.getHintAndSol())
                .build();
    }


}

