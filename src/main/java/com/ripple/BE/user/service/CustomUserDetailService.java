package com.ripple.BE.user.service;

import static com.ripple.BE.user.exception.errorcode.UserErrorCode.*;

import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // pk 받아서 해당 유저를 찾아 CustomUserDetails로 반환
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findById(Long.parseLong(id))
                        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));

        return new CustomUserDetails(user.getId(), user.getNickname());
    }
}
