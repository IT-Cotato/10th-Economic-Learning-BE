package com.ripple.BE.learning.validation;

import com.ripple.BE.learning.domain.quiz.Quiz;
import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.learning.dto.request.SubmitAnswerRequest;
import com.ripple.BE.learning.service.quiz.QuizService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnswerIndexValidator
        implements ConstraintValidator<ValidAnswerIndex, SubmitAnswerRequest> {

    private static final int OX_MAX_ANSWER_INDEX = 1;
    private static final int MULTIPLE_CHOICE_MAX_ANSWER_INDEX = 3;
    private static final int MIN_ANSWER_INDEX = 0;

    private final QuizService quizService;

    @Override
    public boolean isValid(
            SubmitAnswerRequest submitAnswerRequest, ConstraintValidatorContext context) {

        Quiz quiz = quizService.getQuizById(submitAnswerRequest.quizId());
        Type type = submitAnswerRequest.type();
        int answerIndex = submitAnswerRequest.answerIndex();

        if (quiz.getType() != type) { // 퀴즈 타입과 요청 타입이 다르면 유효하지 않음
            return false;
        }

        return switch (type) {
            case OX -> answerIndex == MIN_ANSWER_INDEX
                    || answerIndex == OX_MAX_ANSWER_INDEX; // OX 퀴즈는 0 또는 1만 허용
            case MULTIPLE_CHOICE_LONG, MULTIPLE_CHOICE_SHORT -> answerIndex >= MIN_ANSWER_INDEX
                    && answerIndex <= MULTIPLE_CHOICE_MAX_ANSWER_INDEX; // 객관식 퀴즈는 0, 1, 2, 3만 허용
            default -> false;
        };
    }
}
