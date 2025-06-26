package com.ripple.BE.learning.persistence.jpa.repository.failquiz;

import com.ripple.BE.learning.persistence.jpa.entity.quiz.FailQuizJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailQuizJpaRepository extends JpaRepository<FailQuizJpaEntity, Long> {
    void deleteAllByUserId(Long userId);
}
