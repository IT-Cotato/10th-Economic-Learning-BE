package com.ripple.BE.user.repository;

import com.ripple.BE.user.domain.User;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKeyCode(String keyCode);

    Optional<User> findByAccountEmail(String accountEmail);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u")
    List<User> findAllWithLock();
}
