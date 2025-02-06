package com.ripple.BE.user.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.dto.QuestDTO;
import com.ripple.BE.user.dto.response.QuestResponse;
import com.ripple.BE.user.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attencance")
@Tag(name = "Attendance", description = "출석 API")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "연속 출석 날짜 조회", description = "현재 사용자의 연속 출석 날짜를 조회합니다.")
    @GetMapping("/current-streak")
    public ResponseEntity<ApiResponse<?>> currentStreak(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long currentStreak = attendanceService.getCurrentStreak(customUserDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(currentStreak));
    }

    @Operation(
            summary = "오늘의 퀘스트 완료 여부 조회",
            description = "오늘의 퀘스트 완료 여부를 조회합니다. 퍼센트로 반환됩니다 (0~100). 매일 0시 1분 0초에 자동으로 초기화됩니다.")
    @GetMapping("/today-quest")
    public ResponseEntity<ApiResponse<?>> todayQuest(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        QuestDTO todayQuest = attendanceService.getTodayQuest(customUserDetails.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(QuestResponse.toQuestResponse(todayQuest)));
    }

    @Operation(
            summary = "요일별 출석 현황 조회",
            description = "요일별 출석 현황을 조회합니다. 매주 월요일 0시 0분 0초에 자동으로 초기화됩니다.")
    @GetMapping("/weekly-attendance")
    public ResponseEntity<ApiResponse<?>> weeklyAttendance(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.from(attendanceService.getWeeklyAttendance(customUserDetails.getId())));
    }
}
