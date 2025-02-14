package com.example.game.Controller;

import com.example.game.Model.Game;
import com.example.game.Model.Question;
import com.example.game.Service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public Game startGame(@RequestParam String name, @RequestParam String difficulty) {
        return gameService.startGame(name, difficulty);
    }

    @PostMapping("/{gameId}/submit")
    public String submitAnswer(@PathVariable Long gameId, @RequestParam float answer) {
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            return "Game not found.";
        }

        Question currentQuestion = gameService.getCurrentQuestion(gameId);
        if (currentQuestion == null) {
            return "No active question found.";
        }

        float startTime = currentQuestion.getStartTime();
        long endTime = System.currentTimeMillis();
        float timeTaken = (endTime - startTime) / 1000.0f;

        currentQuestion.setPlayerAnswer(answer);
        currentQuestion.setTimeTaken(timeTaken);
        gameService.saveQuestion(currentQuestion);

        String resultMessage;
        if (answer == currentQuestion.getCorrectAnswer()) {
            resultMessage = "Good job " + game.getPlayerName() + ", your answer is correct!";
        } else {
            resultMessage = "Sorry " + game.getPlayerName() + ", your answer is incorrect.";
        }

        return resultMessage + " Time taken: " + timeTaken + " seconds.";
    }

    @GetMapping("/{gameId}/status")
    public String getGameStatus(@PathVariable Long gameId) {
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            return "Game not found.";
        }

        int totalQuestions = game.getQuestions().size();
        int correctAnswers = (int) game.getQuestions().stream()
                .filter(q -> q.getPlayerAnswer() != null && q.getPlayerAnswer() == q.getCorrectAnswer())
                .count();
        float totalTimeSpent = game.getQuestions().stream()
                .map(Question::getTimeTaken)
                .reduce(0.0f, Float::sum);

        StringBuilder history = new StringBuilder();
        for (Question question : game.getQuestions()) {
            history.append("Question: ").append(question.getQuestionText())
                    .append(", Your Answer: ").append(question.getPlayerAnswer())
                    .append(", Correct Answer: ").append(question.getCorrectAnswer())
                    .append(", Time Taken: ").append(question.getTimeTaken()).append(" seconds\n");
        }

        return "Player: " + game.getPlayerName() + "\n" +
                "Difficulty: " + game.getDifficulty() + "\n" +
                "Current Score: " + correctAnswers + "/" + totalQuestions + "\n" +
                "Total Time Spent: " + totalTimeSpent + " seconds\n" +
                "History:\n" + history.toString();
    }
}