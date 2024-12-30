package com.ripple.BE.user.service;

import static com.ripple.BE.user.domain.User.*;
import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.auth.dto.kakao.KakaoUserInfoResponse;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.LoginType;
import com.ripple.BE.user.dto.UpdateUserProfileRequest;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;
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

    @Transactional
    public void updateQuizAndCorrectCount(User user, int quizCount, int correctCount) {
        user.increaseQuizCount(quizCount);
        user.increaseCorrectCount(correctCount);
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
}
