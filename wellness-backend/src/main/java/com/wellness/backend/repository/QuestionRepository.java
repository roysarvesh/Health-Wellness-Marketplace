package com.wellness.backend.repository;

import com.wellness.backend.model.Question;
import com.wellness.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // All questions by a user
    List<Question> findByUser(User user);

    // Search questions by title
    List<Question> findByTitleContainingIgnoreCase(String keyword);
}
