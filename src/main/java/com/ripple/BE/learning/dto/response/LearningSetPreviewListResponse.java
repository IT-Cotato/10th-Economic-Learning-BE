package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.UserLearningSetListDTO;
import java.util.List;

public record LearningSetPreviewListResponse(
        List<LearningSetPreviewResponse> learningSetPreviewList // 학습 세트 미리보기 리스트
        ) {

    public static LearningSetPreviewListResponse toLearningSetPreviewListResponse(
            final UserLearningSetListDTO userLearningSetListDTO) {

        return new LearningSetPreviewListResponse(
                userLearningSetListDTO.learningSetCompleteList().stream()
                        .map(LearningSetPreviewResponse::toLearningSetPreviewResponse)
                        .toList());
    }
}
