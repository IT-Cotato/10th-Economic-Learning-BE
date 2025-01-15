package com.ripple.BE.learning.dto;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import java.util.List;

public record LearningSetListDTO(List<LearningSetDTO> learningSetList // 학습 세트 리스트
        ) {

    public static LearningSetListDTO toLearningSetListDTO(final List<LearningSet> learningSetList) {
        return new LearningSetListDTO(
                learningSetList.stream().map(LearningSetDTO::toLearningSetDTO).toList());
    }
}
