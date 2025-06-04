package com.ripple.BE.learning.persistence.impl;

import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.learning.persistence.ConceptRepository;
import com.ripple.BE.learning.persistence.jpa.entity.concept.ConceptJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.concept.ConceptJpaRepository;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConceptRepositoryImpl implements ConceptRepository {

    private final ConceptJpaRepository conceptJpaRepository;

    @Override
    public List<Concept> findAllByLearningSetIdAndLevel(final long learningSetId, final Level level) {
        return conceptJpaRepository.findAllByLearningSetIdAndLevel(learningSetId, level).stream()
                .map(ConceptJpaEntity::toModel)
                .toList();
    }

    @Override
    public Optional<Concept> findById(final long id) {
        return conceptJpaRepository.findById(id).map(ConceptJpaEntity::toModel);
    }

    @Override
    public void saveAll(final List<Concept> concepts) {
        List<ConceptJpaEntity> conceptEntities = concepts.stream().map(ConceptJpaEntity::from).toList();
        conceptJpaRepository.saveAll(conceptEntities);
    }

    @Override
    public boolean existsById(final long id) {
        return conceptJpaRepository.existsById(id);
    }
}
