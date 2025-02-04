package com.ripple.BE.user.dto.response;

import com.ripple.BE.user.dto.AttendanceDTO;

public record AttendanceResponse(
        Boolean monday,
        Boolean tuesday,
        Boolean wednesday,
        Boolean thursday,
        Boolean friday,
        Boolean saturday,
        Boolean sunday) {
    public static AttendanceResponse toAttendanceResponse(AttendanceDTO attendanceDTO) {
        return new AttendanceResponse(
                attendanceDTO.monday(),
                attendanceDTO.tuesday(),
                attendanceDTO.wednesday(),
                attendanceDTO.thursday(),
                attendanceDTO.friday(),
                attendanceDTO.saturday(),
                attendanceDTO.sunday());
    }
}
