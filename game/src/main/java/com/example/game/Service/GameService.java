package com.example.game.Service;

import com.example.game.Model.Game;
import com.example.game.Model.Question;
import com.example.game.Repository.GameRepository;
import com.example.game.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Game startGame(String playerName, String difficulty) {
        // Create a new game
        Game game = new Game();
        game.setPlayerName(playerName);
        game.setDifficulty(difficulty);
        game.setStartTime(System.currentTimeMillis());

        // Save the game to the database
        game = gameRepository.save(game);

        // Generate the first question for the game
        generateQuestion(game);

        // Fetch the game with questions using a custom query
        return gameRepository.findGameWithQuestions(game.getGameId());
    }

    public void generateQuestion(Game game) {
        Random rand = new Random();
        int a = rand.nextInt(100); // Random number between 0 and 99
        int b = rand.nextInt(100); // Random number between 0 and 99
        String[] operators = {"+", "-", "*", "/"};
        String operator = operators[rand.nextInt(operators.length)]; // Random operator

        // Generate the question text
        String questionText = a + " " + operator + " " + b;

        // Calculate the correct answer
        float correctAnswer = calculateAnswer(a, b, operator);

        // Create a new question
        Question question = new Question();
        question.setQuestionText(questionText);
        question.setCorrectAnswer(correctAnswer);
        question.setStartTime((float) System.currentTimeMillis());

        // Associate the question with the game
        game.addQuestion(question);

        // Save the question to the database
        questionRepository.save(question);
    }

    private float calculateAnswer(int a, int b, String operator) {
        switch (operator) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return (float) a / b;
            default: throw new IllegalArgumentException("Invalid operator");
        }
    }

    // Retrieve a game by its ID
    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId).orElse(null);
    }

    // Retrieve the current (latest) question for a game
    public Question getCurrentQuestion(Long gameId) {
        return questionRepository.findTopByGameGameIdOrderByQuestionIdDesc(gameId);
    }

    // Save a question (e.g., after the player submits an answer)
    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    // Retrieve all questions for a game
    public List<Question> getQuestionsByGameId(Long gameId) {
        return questionRepository.findByGameGameId(gameId);
    }
}