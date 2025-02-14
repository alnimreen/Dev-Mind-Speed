package com.example.game.Repository;

import com.example.game.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findTopByGameGameIdOrderByQuestionIdDesc(Long gameId);

    List<Question> findByGameGameId(Long gameId);
}