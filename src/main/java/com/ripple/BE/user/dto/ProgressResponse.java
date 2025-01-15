package com.ripple.BE.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ripple.BE.user.domain.type.Level;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProgressResponse(Map<Level, Double> progress) {

    public static ProgressResponse toProgressResponse(final ProgressDTO levelDTO) {
        return new ProgressResponse(levelDTO.progress());
    }
}
