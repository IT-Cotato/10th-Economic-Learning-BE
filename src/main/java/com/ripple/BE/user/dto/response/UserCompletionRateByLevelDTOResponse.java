package com.ripple.BE.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ripple.BE.user.domain.type.Level;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserCompletionRateByLevelDTOResponse(Map<Level, Integer> progress) {

    public static UserCompletionRateByLevelDTOResponse from(Map<Level, Double> levelProgress) {
        if (levelProgress == null || levelProgress.isEmpty()) {
            return new UserCompletionRateByLevelDTOResponse(Map.of());
        }

        Map<Level, Integer> progressMap =
                levelProgress.entrySet().stream()
                        .collect(
                                Collectors.toMap(Map.Entry::getKey, entry -> (int) Math.round(entry.getValue())));

        return new UserCompletionRateByLevelDTOResponse(progressMap);
    }
}
