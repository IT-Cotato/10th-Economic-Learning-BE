package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Map;

public record QuizDTO(
        Long id,
        Purpose purpose,
        Type type,
        Level level,
        String question,
        String answer,
        String wrongAnswer,
        List<String> options,
        String explanation,
        String learningSetName) {

    private static final String PURPOSE = "QUIZ";
    private static final String TYPE = "type";
    private static final String LEVEL = "level";
    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";
    private static final String WRONG_ANSWER = "wrong_answer";
    private static final String EXPLANATION = "explanation";
    private static final String LEARNING_SET_NAME = "learning_set_name";

    public static QuizDTO toQuizDTO(final Quiz quiz) {
        return new QuizDTO(
                quiz.getId(),
                quiz.getPurpose(),
                quiz.getType(),
                quiz.getLevel(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getWrongAnswer(),
                null,
                quiz.getExplanation(),
                null);
    }

    public static QuizDTO toQuizDTO(final Quiz quiz, final List<String> options) {
        return new QuizDTO(
                quiz.getId(),
                quiz.getPurpose(),
                quiz.getType(),
                quiz.getLevel(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getWrongAnswer(),
                options,
                quiz.getExplanation(),
                null);
    }

    public static QuizDTO toQuizDTO(Map<String, String> excelData) {
        return new QuizDTO(
                null,
                Purpose.valueOf(PURPOSE),
                Type.valueOf(excelData.get(TYPE)),
                Level.valueOf(excelData.get(LEVEL)),
                excelData.get(QUESTION),
                excelData.get(ANSWER),
                excelData.get(WRONG_ANSWER),
                null,
                excelData.get(EXPLANATION),
                excelData.get(LEARNING_SET_NAME));
    }
}
