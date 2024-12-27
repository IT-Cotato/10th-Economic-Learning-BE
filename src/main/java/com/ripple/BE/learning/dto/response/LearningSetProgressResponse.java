package com.ripple.BE.learning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ripple.BE.learning.dto.ProgressDTO;
import com.ripple.BE.user.domain.type.Level;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LearningSetProgressResponse(Map<Level, Double> progress) {

    public static LearningSetProgressResponse toLearningSetProgressResponse(
            final ProgressDTO levelDTO) {
        return new LearningSetProgressResponse(levelDTO.progress());
    }
}
