package com.ripple.BE.user.dto.response;

import com.ripple.BE.user.dto.UserRandomProfileListDTO;
import java.util.List;

public record UserRandomProfileListResponse(
        List<UserRandomProfileResponse> userRandomProfileResponseList) {

    public static UserRandomProfileListResponse toUserRandomProfileListResponse(
            UserRandomProfileListDTO userRandomProfileListDTO) {
        return new UserRandomProfileListResponse(
                userRandomProfileListDTO.userRandomProfileDTOList().stream()
                        .map(UserRandomProfileResponse::toUserRandomProfileResponse)
                        .toList());
    }
}
