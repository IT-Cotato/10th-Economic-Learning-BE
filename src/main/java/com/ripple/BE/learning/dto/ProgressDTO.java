package com.ripple.BE.learning.dto;

import com.ripple.BE.user.domain.type.Level;
import java.util.Map;

public record ProgressDTO(Map<Level, Double> progress) {

    public static ProgressDTO toProgressDTO(final Map<Level, Double> progress) {
        return new ProgressDTO(progress);
    }
}
