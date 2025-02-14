package com.example.game.Service;

import com.example.game.Model.Game;
import com.example.game.Model.Question;
import com.example.game.Repository.GameRepository;
import com.example.game.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Game startGame(String playerName, String difficulty) {
        Game game = new Game();
        game.setPlayerName(playerName);
        game.setDifficulty(difficulty);
        game.setStartTime(System.currentTimeMillis());

        game = gameRepository.save(game);

        generateQuestion(game,difficulty);

        return gameRepository.findGameWithQuestions(game.getGameId());
    }
    public void generateQuestion(Game game, String difficulty) {
        Random rand = new Random();
        String[] operators = {"+", "-", "*", "/"};
        String operator = operators[rand.nextInt(operators.length)];

        String result = "";
        int max;
        int min;
        int range;
        ArrayList<Integer> num = new ArrayList<>();
        switch (difficulty) {
            case "1":
                max = 10;
                min = 1;
                range = max - min;
                for (int i = 0; i < 2; i++) {
                    num.add((int) (Math.random() * range) + min);
                }
                result = num.get(0) + " " + operator + " " + num.get(1) + " =";
                break;
            case "2":
                max = 100;
                min = 10;
                range = max - min;
                for (int i = 0; i < 3; i++) {
                    num.add((int) (Math.random() * range) + min);
                }
                result = num.get(0) + " " + operator + " " + num.get(1) + " " + operator + " " + num.get(2) + " =";
                break;
            case "3":
                max = 1000;
                min = 100;
                range = max - min;
                for (int i = 0; i < 4; i++) {
                    num.add((int) (Math.random() * range) + min);
                }
                result = num.get(0) + " " + operator + " " + num.get(1) + " " + operator + " " + num.get(2) + " " + operator + " " + num.get(3) + " =";
                break;
            case "4":
                max = 10000;
                min = 1000;
                range = max - min;
                for (int i = 0; i < 5; i++) {
                    num.add((int) (Math.random() * range) + min);
                }
                result = num.get(0) + " " + operator + " " + num.get(1) + " " + operator + " " + num.get(2) + " " + operator + " " + num.get(3) + " " + operator + " " + num.get(4) + " =";
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }

        float correctAnswer = calculateAnswer(num, operator);

        Question question = new Question();
        question.setQuestionText(result);
        question.setCorrectAnswer(correctAnswer);
        question.setStartTime((float) System.currentTimeMillis());

        game.addQuestion(question);

        questionRepository.save(question);
    }

    private float calculateAnswer(ArrayList<Integer> numbers, String operator) {
        float result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            switch (operator) {
                case "+":
                    result += numbers.get(i);
                    break;
                case "-":
                    result -= numbers.get(i);
                    break;
                case "*":
                    result *= numbers.get(i);
                    break;
                case "/":
                    result /= numbers.get(i);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operator");
            }
        }
        return result;
    }
    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId).orElse(null);
    }

    public Question getCurrentQuestion(Long gameId) {
        return questionRepository.findTopByGameGameIdOrderByQuestionIdDesc(gameId);
    }

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public List<Question> getQuestionsByGameId(Long gameId) {
        return questionRepository.findByGameGameId(gameId);
    }
}