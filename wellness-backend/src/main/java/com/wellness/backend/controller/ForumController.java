package com.wellness.backend.controller;

import com.wellness.backend.dto.*;
import com.wellness.backend.model.*;
import com.wellness.backend.repository.*;
import com.wellness.backend.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
@CrossOrigin(origins = "*")
public class ForumController {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public ForumController(
            QuestionRepository questionRepository,
            AnswerRepository answerRepository
    ) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    // =========================
    // Ask a question
    // =========================
    @PostMapping("/questions")
    public ResponseEntity<Question> askQuestion(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody QuestionRequest request
    ) {
        Question question = Question.builder()
                .user(userDetails.getUser())
                .title(request.getTitle())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(questionRepository.save(question));
    }

    // =========================
    // List all questions
    // =========================
    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    // =========================
    // Answer a question
    // =========================
    @PostMapping("/questions/{id}/answers")
    public ResponseEntity<Answer> answerQuestion(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AnswerRequest request
    ) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Answer answer = Answer.builder()
                .question(question)
                .user(userDetails.getUser())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(answerRepository.save(answer));
    }

    // =========================
    // View question + answers (CLEAN RESPONSE)
    // =========================
    @GetMapping("/questions/{id}/answers")
    public ResponseEntity<QuestionAnswerResponse> getAnswers(@PathVariable Long id) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        List<Answer> answers = answerRepository.findByQuestion(question);

        // Map Question → QuestionDTO
        QuestionDTO questionDTO = new QuestionDTO(
                question.getId(),
                question.getTitle(),
                question.getDescription(),
                question.getCreatedAt()
        );

        // Map Answers → AnswerDTO list
        List<AnswerDTO> answerDTOs = answers.stream()
                .map(answer -> new AnswerDTO(
                        answer.getId(),
                        answer.getContent(),
                        answer.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(
                new QuestionAnswerResponse(questionDTO, answerDTOs)
        );
    }
}
