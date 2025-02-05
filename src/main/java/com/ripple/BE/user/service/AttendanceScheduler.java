package com.ripple.BE.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AttendanceScheduler {

    private final AttendanceService attendanceService;

    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 0시 0분 0초에 실행
    @Transactional
    public void resetWeeklyAttendanceLog() {
        attendanceService.resetWeeklyAttendanceLog();
    }

    @Scheduled(cron = "30 0 0 * * ?") //  매일 0시 0분 30초에 실행
    @Transactional
    public void recordAttendanceLog() {
        attendanceService.recordAttendanceLog();
    }

    @Scheduled(cron = "0 1 0 * * ?") // 매일 0시 1분 0초에 실행
    @Transactional
    public void resetAllQuests() {
        attendanceService.resetAllQuests();
    }
}
