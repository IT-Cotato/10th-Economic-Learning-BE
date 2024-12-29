package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.LearningSetCompleteDTO;

// 전체 개념 학습 보기 응답
public record LearningSetPreviewResponse(
        Long id, // 학습 세트 ID
        String name, // 학습 세트 이름
        boolean isLearningSetCompleted, // 학습 세트 완료 여부
        boolean isConceptCompleted, // 개념 학습 완료 여부
        boolean isQuizCompleted // 퀴즈 완료 여부
        ) {

    public static LearningSetPreviewResponse toLearningSetPreviewResponse(
            final LearningSetCompleteDTO learningSetCompleteDTO) {
        return new LearningSetPreviewResponse(
                learningSetCompleteDTO.learningSetId(),
                learningSetCompleteDTO.name(),
                learningSetCompleteDTO.isLearningSetCompleted(),
                learningSetCompleteDTO.isConceptCompleted(),
                learningSetCompleteDTO.isQuizCompleted());
    }
}
