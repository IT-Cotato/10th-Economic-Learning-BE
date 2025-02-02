package com.ripple.BE.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ripple.BE.user.domain.type.Level;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProgressResponse(Map<Level, Integer> progress) {

    public static ProgressResponse toProgressResponse(final ProgressDTO levelDTO) {

        Map<Level, Integer> progressMap =
                levelDTO.progress().entrySet().stream()
                        .collect(
                                Collectors.toMap(Map.Entry::getKey, entry -> (int) Math.round(entry.getValue())));

        return new ProgressResponse(progressMap);
    }
}
