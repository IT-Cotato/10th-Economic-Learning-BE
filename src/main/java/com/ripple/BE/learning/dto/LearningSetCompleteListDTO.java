package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.LearningSetComplete;
import java.util.List;

public record LearningSetCompleteListDTO(
        List<LearningSetCompleteDTO> learningSetCompleteList // 학습 세트 완료 리스트
        ) {

    public static LearningSetCompleteListDTO toLearningSetCompleteListDTO(
            final List<LearningSetComplete> learningSetCompleteList) {
        return new LearningSetCompleteListDTO(
                learningSetCompleteList.stream()
                        .map(LearningSetCompleteDTO::toLearningSetCompleteDTO)
                        .toList());
    }
}
