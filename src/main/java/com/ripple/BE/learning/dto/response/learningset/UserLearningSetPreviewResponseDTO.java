package com.ripple.BE.learning.dto.response.learningset;

import com.ripple.BE.learning.domain.learningset.UserLearningSet;

// 전체 개념 학습 보기 응답, 사용자 학습 세트 미리보기
public record UserLearningSetPreviewResponseDTO(
        Long learningSetId, // 학습 세트 ID
        String name, // 학습 세트 이름
        boolean isLearningSetCompleted, // 학습 세트 완료 여부
        boolean isConceptCompleted, // 개념 학습 완료 여부
        boolean isQuizCompleted // 퀴즈 완료 여부
        ) {

    public static UserLearningSetPreviewResponseDTO from(UserLearningSet userLearningSet) {
        return new UserLearningSetPreviewResponseDTO(
                userLearningSet.getLearningSetId(),
                userLearningSet.getLearningSetName(),
                userLearningSet.isLearningSetCompleted(),
                userLearningSet.isConceptCompleted(),
                userLearningSet.isQuizCompleted());
    }
}
