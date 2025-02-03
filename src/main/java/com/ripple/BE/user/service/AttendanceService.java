package com.ripple.BE.user.service;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.user.domain.Attendance;
import com.ripple.BE.user.domain.AttendanceLog;
import com.ripple.BE.user.domain.Quest;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.AttendanceLogRepository;
import com.ripple.BE.user.repository.AttendanceRepository;
import com.ripple.BE.user.repository.QuestRepository;
import com.ripple.BE.user.repository.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class AttendanceService {

    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceLogRepository attendanceLogRepository;

    public void getCurrentStreak(Long id) {
        // TODO Auto-generated method stub
    }

    public void getTodayQuest(Long id) {
        // TODO Auto-generated method stub
    }

    @Transactional
    public void completeQuest(Long userId, String questType) {
        User user =
                userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        // 오늘의 퀘스트 조회 없으면 생성
        Quest quest =
                questRepository
                        .findByUserIdAndDate(userId, LocalDate.now())
                        .orElseGet(
                                () ->
                                        questRepository.save(Quest.builder().user(user).date(LocalDate.now()).build()));

        // 퀘스트 타입에 따라 완료 처리
        switch (questType) {
            case "QUIZ":
                quest.setQuizCompleted(true);
                break;
            case "CONCEPT":
                quest.setConceptCompleted(true);
                break;
            case "ARTICLE":
                quest.setArticleCompletedCount(quest.getArticleCompletedCount() + 1);
                break;
            default:
                throw new UserException(INVALID_QUEST_TYPE);
        }

        // 퀘스트 3개 완료 시 출석 완료 처리
        if (quest.getArticleCompletedCount() >= 3
                && quest.isConceptCompleted()
                && quest.isQuizCompleted()) {
            completeAttendance(user, userId, quest);
        }
    }

    @Transactional
    public void completeAttendance(User user, Long userId, Quest quest) {
        // 출석 조회 없으면 생성
        Attendance attendance =
                attendanceRepository
                        .findByUserId(userId)
                        .orElseGet(() -> attendanceRepository.save(Attendance.builder().user(user).build()));

        // 연속 출석일 계산
        if (attendance.getLastAttendedDate().plusDays(1).isEqual(LocalDate.now())) {
            attendance.setCurrentStreak(attendance.getCurrentStreak() + 1);
        } else {
            attendance.setCurrentStreak(1L);
            attendance.setLastResetDate(LocalDate.now());
        }

        // 출석 로그 생성
        AttendanceLog.builder().attendance(attendance).date(LocalDate.now()).isAttended(true).build();
    }
}
