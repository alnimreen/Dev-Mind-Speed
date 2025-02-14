package com.example.game.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    private String questionText;
    private Float correctAnswer;
    private Float playerAnswer;
    private Float timeTaken;
    private Float startTime;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private Game game;

    public Long getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Float getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Float correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Float getPlayerAnswer() {
        return playerAnswer;
    }

    public void setPlayerAnswer(Float playerAnswer) {
        this.playerAnswer = playerAnswer;
    }

    public Float getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Float timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setStartTime(float startTime) {
    this.startTime=startTime;
    }

    public float getStartTime() {
    return startTime;}
}