package com.ripple.BE.auth.service;

import com.ripple.BE.auth.client.KakaoApiClient;
import com.ripple.BE.auth.dto.KakaoUserInfoResponse;
import com.ripple.BE.auth.jwt.JwtTokenProvider;
import com.ripple.BE.user.service.UserService;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class KakaoAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

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
}
