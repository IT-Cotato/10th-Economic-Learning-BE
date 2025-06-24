package com.ripple.BE.user.repository;

import com.ripple.BE.user.domain.Quest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {

    Optional<Quest> findByUserId(Long userId);

    List<Quest> findAll();

    void deleteAllByUserId(Long userId);
}
