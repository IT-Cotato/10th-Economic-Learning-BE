package com.ripple.BE.learning.persistence.impl;

import com.ripple.BE.learning.domain.learningset.LearningSetStat;
import com.ripple.BE.learning.persistence.LearningSetStatRepository;
import com.ripple.BE.learning.persistence.jpa.entity.learningset.LearningSetStatJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.learningset.LearingSetStatJpaRepository;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LearningSetStatRepositoryImpl implements LearningSetStatRepository {

    private final LearingSetStatJpaRepository learningSetStatJpaRepository;

    @Override
    public List<LearningSetStat> findAll() {

        return learningSetStatJpaRepository.findAll().stream()
                .map(LearningSetStatJpaEntity::toModel)
                .toList();
    }

    @Override
    public LearningSetStat save(final LearningSetStat learningSetStat) {
        LearningSetStatJpaEntity entity = LearningSetStatJpaEntity.from(learningSetStat);
        return learningSetStatJpaRepository.save(entity).toModel();
    }

    @Override
    public Optional<LearningSetStat> findByLevel(final Level level) {
        return learningSetStatJpaRepository.findByLevel(level).map(LearningSetStatJpaEntity::toModel);
    }
}
