package com.ripple.BE.learning.persistence.jpa.repository.learningset;

import com.ripple.BE.learning.persistence.jpa.entity.learningset.LearningSetStatJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearingSetStatJpaRepository
        extends JpaRepository<LearningSetStatJpaEntity, String> {

    Optional<LearningSetStatJpaEntity> findByLevel(Level level);
}
