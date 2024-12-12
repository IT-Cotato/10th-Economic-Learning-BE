package com.ripple.BE.global.config;

import com.ripple.BE.auth.filter.JwtAuthFilter;
import com.ripple.BE.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(
                                                "/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                                        .permitAll() // /api/v1/auth/** Swagger 경로는 누구나 접근 가능
                                        .anyRequest()
                                        .authenticated() // 그 외의 경로는 인증된 사용자만 접근 가능
                        )
                .addFilterBefore(
                        new JwtAuthFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }
}
