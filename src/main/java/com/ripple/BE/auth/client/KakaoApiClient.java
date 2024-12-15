package com.ripple.BE.auth.client;

import com.ripple.BE.auth.dto.kakao.KakaoTokenResponse;
import com.ripple.BE.auth.dto.kakao.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RequiredArgsConstructor
@Slf4j
@Component
public class KakaoApiClient { // kakao API를 호출하기 위한 전용 class

    private final WebClient webClient;
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;

    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    // 카카오 API 호출 : Authorization code -> Access Token
    public String getAccessToken(String code) {
        try {
            KakaoTokenResponse response =
                    webClient
                            .post()
                            .uri(TOKEN_REQUEST_URI)
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .bodyValue(
                                    "grant_type=authorization_code&client_id="
                                            + kakaoApiKey
                                            + "&redirect_uri="
                                            + kakaoRedirectUri
                                            + "&code="
                                            + code)
                            .retrieve()
                            .bodyToMono(KakaoTokenResponse.class)
                            .block();

            return response.accessToken();

        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to fetch access token from Kakao.", e);
        }
    }

    // 카카오 API 호출 : Access Token -> 사용자 정보 조회
    public KakaoUserInfoResponse getUserInfo(String token) {
        try {
            return webClient
                    .get()
                    .uri(USER_INFO_URI)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(KakaoUserInfoResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to fetch access token from Kakao.", e);
        }
    }
}
