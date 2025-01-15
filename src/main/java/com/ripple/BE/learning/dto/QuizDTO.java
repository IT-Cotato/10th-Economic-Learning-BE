package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Purpose;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.request.AddLevelTestQuizRequest;
import com.ripple.BE.user.domain.type.Level;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record QuizDTO(
        Long id,
        String name,
        Purpose purpose,
        Type type,
        Level level,
        String question,
        String answer,
        ChoiceListDTO choiceList,
        String explanation,
        String learningSetName) {

    private static final String PURPOSE = "QUIZ";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String LEVEL = "level";
    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";
    private static final String EXPLANATION = "explanation";
    private static final String LEARNING_SET_NAME = "learning_set_name";
    private static final String CHOICES = "choices";

    public static QuizDTO toQuizDTO(final Quiz quiz) {
        return new QuizDTO(
                quiz.getId(),
                quiz.getName(),
                quiz.getPurpose(),
                quiz.getType(),
                quiz.getLevel(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getChoices() == null ? null : ChoiceListDTO.toChoiceListDTO(quiz.getChoices()),
                quiz.getExplanation(),
                null);
    }

    public static QuizDTO toQuizDTO(Map<String, String> excelData) {
        List<ChoiceDTO> choices =
                Arrays.stream(excelData.get(CHOICES).split("\\d+\\.\\s*")) // 숫자와 점을 기준으로 분리
                        .filter(choice -> !choice.trim().isEmpty()) // 빈 항목 제거
                        .map(choice -> ChoiceDTO.toChoiceDTO(choice.trim())) // 각 항목에서 개행 문자와 공백 제거
                        .toList();

        return new QuizDTO(
                null,
                excelData.get(NAME),
                Purpose.valueOf(PURPOSE),
                Type.valueOf(excelData.get(TYPE)),
                Level.valueOf(excelData.get(LEVEL)),
                excelData.get(QUESTION),
                excelData.get(ANSWER),
                new ChoiceListDTO(choices),
                excelData.get(EXPLANATION),
                excelData.get(LEARNING_SET_NAME));
    }

    public static QuizDTO toQuizDTO(AddLevelTestQuizRequest request) {
        return new QuizDTO(
                null,
                null,
                Purpose.LEVEL_TEST,
                request.type(),
                null,
                request.question(),
                request.answer(),
                request.choices() == null ? null : ChoiceListDTO.toChoiceListDTO(request),
                request.explanation(),
                null);
    }
}
