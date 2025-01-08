package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.UserLearningSet;
import java.util.List;

public record UserLearningSetListDTO(
        List<UserLearningSetDTO> learningSetCompleteList // 학습 세트 완료 리스트
        ) {

    public static UserLearningSetListDTO toUserLearningSetListDTO(
            final List<UserLearningSet> userLearningSetList) {
        return new UserLearningSetListDTO(
                userLearningSetList.stream().map(UserLearningSetDTO::toUserLearningSetDTO).toList());
    }
}
