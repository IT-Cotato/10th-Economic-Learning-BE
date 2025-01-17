package com.ripple.BE.learning.dto.response;

import com.ripple.BE.learning.dto.FailQuizListDTO;
import java.util.List;

public record FailQuizListResponse(List<FailQuizResponse> failQuizList) {
    public static FailQuizListResponse toFailQuizListResponse(final FailQuizListDTO failQuizListDTO) {
        return new FailQuizListResponse(
                failQuizListDTO.failQuizList().stream().map(FailQuizResponse::toFailQuizResponse).toList());
    }
}
