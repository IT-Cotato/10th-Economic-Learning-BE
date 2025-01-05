package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.request.AddLevelTestQuizRequest;
import java.util.Map;

public record QuizDTO(
        Long id,
        Purpose purpose,
        Type type,
        String question,
        String answer,
        String wrongAnswer,
        String explanation,
        String learningSetNum) {

    private static final String PURPOSE = "QUIZ";
    private static final String TYPE = "type";
    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";
    private static final String WRONG_ANSWER = "wrong_answer";
    private static final String EXPLANATION = "explanation";
    private static final String LEARNING_SET_NUM = "learning_set_num";

    public static QuizDTO toQuizDTO(final Quiz quiz) {
        return new QuizDTO(
                quiz.getId(),
                quiz.getPurpose(),
                quiz.getType(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getWrongAnswer(),
                quiz.getExplanation(),
                null);
    }

    public static QuizDTO toQuizDTO(Map<String, String> excelData) {
        return new QuizDTO(
                null,
                Purpose.valueOf(PURPOSE),
                Type.valueOf(excelData.get(TYPE)),
                excelData.get(QUESTION),
                excelData.get(ANSWER),
                excelData.get(WRONG_ANSWER),
                excelData.get(EXPLANATION),
                excelData.get(LEARNING_SET_NUM));
    }

    public static QuizDTO toQuizDTO(AddLevelTestQuizRequest request) {
        return new QuizDTO(
                null,
                Purpose.LEVEL_TEST,
                request.type(),
                request.question(),
                request.answer(),
                request.wrongAnswer(),
                request.explanation(),
                null);
    }
}
