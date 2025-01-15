package com.ripple.BE.user.service;

import static com.ripple.BE.user.domain.User.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.auth.dto.kakao.KakaoUserInfoResponse;
import com.ripple.BE.learning.domain.quiz.FailQuiz;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.Level;
import com.ripple.BE.user.domain.type.LoginType;
import com.ripple.BE.user.dto.UpdateUserProfileRequest;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long findOrCreateUser(KakaoUserInfoResponse response) {
        User user =
                userRepository
                        .findByKeyCode(response.id().toString())
                        .orElse(
                                User.kakaoBuilder()
                                        .accountEmail(response.kakao_account().email())
                                        .profileImageUrl(response.properties().profile_image())
                                        .loginType(LoginType.KAKAO)
                                        .keyCode(response.id().toString())
                                        .buildKakaoUser());

        userRepository.save(user);

        return user.getId();
    }

    @Transactional
    public void updateProfile(UpdateUserProfileRequest request, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.updateProfile(request);
    }

    @Transactional(readOnly = true)
    public User findUser(String accountEmail) {
        return userRepository
                .findByAccountEmail(accountEmail)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    @Transactional
    public void createUser(String accountEmail, String password) {
        // 이미 존재하는 이메일인지 확인
        userRepository
                .findByAccountEmail(accountEmail)
                .ifPresent(
                        user -> {
                            throw new UserException(INVALID_EMAIL);
                        });

        userRepository.save(
                basicBuilder()
                        .accountEmail(accountEmail)
                        .password(passwordEncoder.encode(password))
                        .buildBasicUser());
    }

    @Transactional
    public void updateUserStatsAfterQuiz(
            User user, Level level, List<FailQuiz> failList, int quizCount, int correctCount) {
        // 완료된 학습 레벨 카운트 증가
        updateCompletedCountByLevel(user, level);

        // 실패한 퀴즈 목록 추가
        user.getFailQuizList().addAll(failList);

        // 퀴즈 수와 정답 수 업데이트
        user.increaseQuizCount(quizCount);
        user.increaseCorrectCount(correctCount);
    }

    @Transactional
    public void updateCompletedCountByLevel(User user, Level level) {
        user.increaseCompletedCountByLevel(level);

        /** TODO: 레벨 업 조건 확인 후 레벨 업 처리 만약 상위 조건의 학습 세트를 완료한 경우 하위 레벨의 학습 세트가 완료되어야지 레벨 업이 가능하다. */
    }
}
