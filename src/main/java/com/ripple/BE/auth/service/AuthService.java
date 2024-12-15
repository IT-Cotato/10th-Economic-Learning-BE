package com.ripple.BE.auth.service;

import com.ripple.BE.auth.client.KakaoApiClient;
import com.ripple.BE.auth.dto.LoginRequest;
import com.ripple.BE.auth.dto.SignupRequest;
import com.ripple.BE.auth.dto.kakao.KakaoUserInfoResponse;
import com.ripple.BE.auth.jwt.JwtTokenProvider;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.service.UserService;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String kakaoLogin(String code) {
        String accessToken =
                kakaoApiClient.getAccessToken(code); // 1. Authorization Code를 Access Token으로 교환

        Long userId = isSignedUp(accessToken); // 2. Access Token을 이용해 사용자 정보를 가져오고 없으면 회원가입

        HashMap<Long, String> map = new HashMap<>();
        map.put(userId, jwtTokenProvider.createToken(userId.toString())); // 3. JWT 토큰을 생성하여 반환한다.

        return map.get(userId);
    }

    @Transactional
    public Long isSignedUp(String token) {
        KakaoUserInfoResponse userInfo = kakaoApiClient.getUserInfo(token);
        return userService.findOrCreateUser(userInfo);
    }

    @Transactional
    public void BasicSignup(SignupRequest request) {
        userService.createUser(request.accountEmail(), request.password());
    }

    public String BasicLogin(LoginRequest request) {
        User user = userService.findUser(request.accountEmail());

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtTokenProvider.createToken(user.getId().toString());
    }
}
