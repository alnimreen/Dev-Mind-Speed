package com.example.game.Repository;

import com.example.game.Model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.questions WHERE g.gameId = :gameId")
    Game findGameWithQuestions(@Param("gameId") Long gameId);
}