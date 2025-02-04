package com.ripple.BE.user.dto;

import lombok.Builder;

@Builder
public record AttendanceDTO(
        Boolean monday,
        Boolean tuesday,
        Boolean wednesday,
        Boolean thursday,
        Boolean friday,
        Boolean saturday,
        Boolean sunday) {}
