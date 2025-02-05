package com.ripple.BE.user.service;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.user.domain.Attendance;
import com.ripple.BE.user.domain.AttendanceLog;
import com.ripple.BE.user.domain.Quest;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.dto.AttendanceDTO;
import com.ripple.BE.user.dto.QuestDTO;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.AttendanceLogRepository;
import com.ripple.BE.user.repository.AttendanceRepository;
import com.ripple.BE.user.repository.QuestRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class AttendanceService {

    private final QuestRepository questRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceLogRepository attendanceLogRepository;

    public Long getCurrentStreak(Long id) {
        Attendance attendance =
                attendanceRepository
                        .findByUserId(id)
                        .orElseThrow(() -> new UserException(ATTENDANCE_NOT_FOUND));

        return attendance.getCurrentStreak();
    }

    public QuestDTO getTodayQuest(Long id) {
        Quest quest =
                questRepository.findByUserId(id).orElseThrow(() -> new UserException(QUEST_NOT_FOUND));

        return QuestDTO.toQuestDTO(
                quest.isConceptCompleted() ? 100L : 0L,
                quest.isQuizCompleted() ? 100L : 0L,
                quest.getArticleCompletedCount() / 3 * 100);
    }

    @Transactional
    public void completeQuest(Long userId, String questType) {
        Quest quest =
                questRepository.findByUserId(userId).orElseThrow(() -> new UserException(QUEST_NOT_FOUND));

        // 퀘스트 타입에 따라 완료 처리
        switch (questType.toUpperCase()) {
            case "QUIZ" -> quest.updateQuizCompleted();
            case "CONCEPT" -> quest.updateConceptCompleted();
            case "ARTICLE" -> quest.updateArticleCompletedCount();
            default -> throw new UserException(INVALID_QUEST_TYPE);
        }

        // 퀘스트 3개 완료 시 출석 완료 처리
        if (quest.getArticleCompletedCount() >= 3
                && quest.isConceptCompleted()
                && quest.isQuizCompleted()) {
            completeAttendance(userId, LocalDate.now());
        }
    }

    @Transactional
    public void completeAttendance(Long userId, LocalDate today) {
        Attendance attendance =
                attendanceRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new UserException(ATTENDANCE_NOT_FOUND));

        // 이미 출석 완료한 경우
        if (attendance.getLastAttendedDate() != null
                && attendance.getLastAttendedDate().equals(today)) {
            return;
        }

        // 연속 출석일 계산
        if (attendance.getLastAttendedDate() != null
                && attendance.getLastAttendedDate().plusDays(1).isEqual(today)) {
            attendance.updateCurrentStreak(attendance.getCurrentStreak() + 1);
        } else {
            attendance.updateCurrentStreak(1L);
        }
        attendance.updateLastAttendedDate(today);

        // 출석 로그 업데이트
        attendanceLogRepository
                .findByAttendanceIdAndDate(attendance.getId(), today)
                .ifPresent(AttendanceLog::updateIsAttended);
    }

    @Transactional
    public void createAttendance(User user) {
        LocalDate today = LocalDate.now();

        Attendance attendance =
                attendanceRepository.save(Attendance.builder().user(user).currentStreak(0L).build());
        questRepository.save(Quest.builder().user(user).lastUpdatedDate(today).build());
        attendanceLogRepository.save(
                AttendanceLog.builder().date(today).isAttended(false).attendance(attendance).build());
    }

    @Transactional
    public AttendanceDTO getWeeklyAttendance(Long userId) {
        Attendance attendance =
                attendanceRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new UserException(ATTENDANCE_NOT_FOUND));

        List<AttendanceLog> attendanceLogList =
                attendanceLogRepository.findByAttendanceId(attendance.getId());
        List<Boolean> weeklyAttendance = new ArrayList<>(Collections.nCopies(7, false));

        for (AttendanceLog attendanceLog : attendanceLogList) {
            int dayIndex = attendanceLog.getDate().getDayOfWeek().getValue() - 1;
            weeklyAttendance.set(dayIndex, attendanceLog.isAttended());
        }

        return AttendanceDTO.builder()
                .monday(weeklyAttendance.get(0))
                .tuesday(weeklyAttendance.get(1))
                .wednesday(weeklyAttendance.get(2))
                .thursday(weeklyAttendance.get(3))
                .friday(weeklyAttendance.get(4))
                .saturday(weeklyAttendance.get(5))
                .sunday(weeklyAttendance.get(6))
                .build();
    }

    @Transactional
    public void resetWeeklyAttendanceLog() {
        attendanceLogRepository.deleteAll();
    }

    @Transactional
    public void resetAllQuests() {
        questRepository.findAll().forEach(Quest::resetQuests);
    }

    @Transactional
    public void recordAttendanceLog() {
        attendanceRepository
                .findAll()
                .forEach(
                        attendance -> {
                            attendanceLogRepository.save(
                                    AttendanceLog.builder()
                                            .attendance(attendance)
                                            .date(LocalDate.now())
                                            .isAttended(false)
                                            .build());
                        });
    }
}
