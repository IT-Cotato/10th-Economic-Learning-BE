package com.ripple.BE.user.dto;

import com.ripple.BE.user.domain.User;
import java.util.List;

public record UserRandomProfileListDTO(List<UserRandomProfileDTO> userRandomProfileDTOList) {
    public static UserRandomProfileListDTO toUserRandomProfileListDTO(List<User> userList) {
        return new UserRandomProfileListDTO(
                userList.stream().map(UserRandomProfileDTO::toUserRandomProfileDTO).toList());
    }
}
