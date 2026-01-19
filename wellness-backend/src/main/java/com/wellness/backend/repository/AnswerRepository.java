package com.wellness.backend.repository;

import com.wellness.backend.model.Answer;
import com.wellness.backend.model.Question;
import com.wellness.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // All answers for a question
    List<Answer> findByQuestion(Question question);

    // All answers given by a user
    List<Answer> findByUser(User user);
}
