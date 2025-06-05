package com.ripple.BE.learning.persistence.jpa.repository.quiz;

import com.ripple.BE.learning.persistence.jpa.entity.quiz.QuizJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizJpaRepository extends JpaRepository<QuizJpaEntity, Long>, QuizQueryRepository {

    @Query(
            "SELECT q FROM QuizJpaEntity q "
                    + "LEFT JOIN FETCH q.choices "
                    + "WHERE q.learningSetId = :learningSetId AND q.level = :level")
    List<QuizJpaEntity> findAllByLearningSetIdAndLevel(final long learningSetId, final Level level);
}
