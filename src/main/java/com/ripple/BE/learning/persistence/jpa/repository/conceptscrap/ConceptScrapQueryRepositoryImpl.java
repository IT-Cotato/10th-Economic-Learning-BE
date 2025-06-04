package com.ripple.BE.learning.persistence.jpa.repository.conceptscrap;

import static com.ripple.BE.learning.persistence.jpa.entity.concept.QConceptJpaEntity.*;
import static com.ripple.BE.learning.persistence.jpa.entity.concept.QConceptScrapJpaEntity.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.learning.persistence.jpa.entity.concept.ConceptJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConceptScrapQueryRepositoryImpl implements ConceptScrapQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ConceptJpaEntity> findConceptsScrappedByUserAndLevel(Long userId, Level level) {
        return queryFactory
                .select(conceptJpaEntity)
                .from(conceptScrapJpaEntity)
                .join(conceptJpaEntity)
                .on(conceptJpaEntity.id.eq(conceptScrapJpaEntity.conceptId))
                .where(conceptScrapJpaEntity.userId.eq(userId), conceptJpaEntity.level.eq(level))
                .fetch();
    }
}
