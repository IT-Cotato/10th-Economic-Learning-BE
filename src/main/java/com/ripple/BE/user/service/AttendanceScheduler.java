package com.ripple.BE.user.service;

import com.ripple.BE.user.domain.AttendanceLog;
import com.ripple.BE.user.domain.Quest;
import com.ripple.BE.user.repository.AttendanceLogRepository;
import com.ripple.BE.user.repository.AttendanceRepository;
import com.ripple.BE.user.repository.QuestRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AttendanceScheduler {

    private final AttendanceLogRepository attendanceLogRepository;
    private final QuestRepository questRepository;
    private final AttendanceRepository attendanceRepository;

    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 0시 0분 0초에 실행
    public void resetWeeklyAttendance() {
        attendanceLogRepository.deleteAll();
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 0시 0분 0초에 실행
    @Transactional
    public void resetAllQuests() {
        questRepository.findAll().forEach(Quest::resetQuests);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 0시 0분 0초에 실행
    @Transactional
    public void recordAttendanceLog() {
        LocalDate today = LocalDate.now();

        attendanceRepository
                .findAll()
                .forEach(
                        attendance -> {
                            attendanceLogRepository.save(
                                    AttendanceLog.builder()
                                            .attendance(attendance)
                                            .date(today)
                                            .isAttended(false)
                                            .build());
                        });
    }
}
