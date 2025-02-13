package com.ripple.BE.user.dto.response;

import com.ripple.BE.user.dto.UserRandomProfileDTO;
import java.util.List;

public record UserRandomProfileListResponse(
        List<UserRandomProfileResponse> userRandomProfileResponseList) {
    public static UserRandomProfileListResponse toUserRandomProfileListResponse(
            List<UserRandomProfileDTO> userRandomProfileListDTOList) {
        return new UserRandomProfileListResponse(
                userRandomProfileListDTOList.stream()
                        .map(UserRandomProfileResponse::toUserRandomProfileResponse)
                        .toList());
    }
}
