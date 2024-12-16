package com.ripple.BE.user.repository;

import com.ripple.BE.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKeyCode(String keyCode);

    Optional<User> findByAccountEmail(String accountEmail);
}
