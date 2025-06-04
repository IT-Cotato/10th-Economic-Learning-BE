package com.ripple.BE.learning.persistence;

import com.ripple.BE.learning.domain.quiz.FailQuiz;
import java.util.List;

public interface FailQuizRepository {

    void saveAll(List<FailQuiz> failQuizzes);
}
