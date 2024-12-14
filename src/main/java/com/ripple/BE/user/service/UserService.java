package com.ripple.BE.user.service;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.auth.dto.KakaoUserInfoResponse;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.domain.type.LoginType;
import com.ripple.BE.user.dto.AddUserProfileRequest;
import com.ripple.BE.user.exception.UserException;
import com.ripple.BE.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long findOrCreateUser(KakaoUserInfoResponse response) {
        User user =
                userRepository
                        .findByKeyCode(response.id().toString())
                        .orElseGet(() -> createUser(response));

        return user.getId();
    }

    @Transactional
    public User createUser(KakaoUserInfoResponse response) {
        User user =
                User.builder()
                        .accountEmail(response.kakao_account().email())
                        .profileImageUrl(response.properties().profile_image())
                        .loginType(LoginType.KAKAO)
                        .keyCode(response.id().toString())
                        .build();

        return userRepository.save(user);
    }

    @Transactional
    public void updateProfile(AddUserProfileRequest request, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        user.updateProfile(request);
    }
}
