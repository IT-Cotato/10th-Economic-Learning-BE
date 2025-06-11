package com.ripple.BE.learning.persistence.jpa.repository.quizscrap;

import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizScrapJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizScrapJpaRepository
        extends JpaRepository<QuizScrapJpaEntity, Long>, QuizScrapQueryRepository {

    boolean existsByQuizIdAndUserId(Long quizId, Long userId);

    Optional<QuizScrapJpaEntity> findByQuizIdAndUserId(Long quizId, Long userId);

    void deleteByQuizIdAndUserId(Long quizId, Long userId);
}
