package com.ripple.BE.learning.persistence.impl;

import com.ripple.BE.learning.domain.learningset.LearningSet;
import com.ripple.BE.learning.persistence.LearningSetRepository;
import com.ripple.BE.learning.persistence.jpa.entity.learningset.LearningSetJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.learningset.LearningSetJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LearningSetRepositoryImpl implements LearningSetRepository {

    private final LearningSetJpaRepository learningSetJpaRepository;

    @Override
    public Optional<String> getLearningSetNameById(final long learningSetId) {
        return learningSetJpaRepository.findNameById(learningSetId);
    }

    @Override
    public Optional<LearningSet> findById(final long learningSetId) {
        return learningSetJpaRepository.findById(learningSetId).map(LearningSetJpaEntity::toModel);
    }

    @Override
    public List<LearningSet> findAll() {
        return learningSetJpaRepository.findAll().stream().map(LearningSetJpaEntity::toModel).toList();
    }

    @Override
    public List<LearningSet> saveAll(final List<LearningSet> learningSets) {
        List<LearningSetJpaEntity> learningSetEntities =
                learningSets.stream().map(LearningSetJpaEntity::from).toList();
        return learningSetJpaRepository.saveAll(learningSetEntities).stream()
                .map(LearningSetJpaEntity::toModel)
                .toList();
    }
}
