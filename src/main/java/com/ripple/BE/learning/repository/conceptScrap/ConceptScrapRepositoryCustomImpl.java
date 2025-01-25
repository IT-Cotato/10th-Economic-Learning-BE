package com.ripple.BE.learning.repository.conceptScrap;

import static com.ripple.BE.learning.domain.concept.QConcept.*;
import static com.ripple.BE.learning.domain.concept.QConceptScrap.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.learning.domain.concept.Concept;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConceptScrapRepositoryCustomImpl implements ConceptScrapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Concept> findConceptsScrappedByUserAndLevel(Long userId, Level level) {
        return queryFactory
                .select(concept)
                .from(conceptScrap)
                .join(conceptScrap.concept, concept)
                .where(conceptScrap.user.id.eq(userId), concept.level.eq(level))
                .fetch();
    }
}
