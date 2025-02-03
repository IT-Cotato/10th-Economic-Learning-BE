package com.ripple.BE.user.service;

import com.ripple.BE.user.repository.AttendanceLogRepository;
import com.ripple.BE.user.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceScheduler {

    private final AttendanceLogRepository attendanceLogRepository;
    private final QuestRepository questRepository;

    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 0시 0분 0초에 실행
    public void resetWeeklyAttendance() {
        attendanceLogRepository.deleteAll();
    }
}
