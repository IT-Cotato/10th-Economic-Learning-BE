package com.ripple.BE.post.repository;

import com.ripple.BE.post.domain.TodayToktok;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodayToktokRepository extends JpaRepository<TodayToktok, Long> {
    Optional<TodayToktok> findBySelectedDate(LocalDate selectedDate);
}
