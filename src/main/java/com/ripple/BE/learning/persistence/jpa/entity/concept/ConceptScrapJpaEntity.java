package com.ripple.BE.learning.persistence.jpa.entity.concept;

import com.ripple.BE.learning.domain.concept.ConceptScrap;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "concept_scraps")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConceptScrapJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "concept_id", nullable = false)
    private Long conceptId;

    @Builder(access = AccessLevel.PRIVATE)
    private ConceptScrapJpaEntity(Long id, Long userId, Long conceptId) {
        this.id = id;
        this.userId = userId;
        this.conceptId = conceptId;
    }

    public static ConceptScrapJpaEntity from(ConceptScrap conceptScrap) {
        return ConceptScrapJpaEntity.builder()
                .id(conceptScrap.getId())
                .userId(conceptScrap.getUserId())
                .conceptId(conceptScrap.getConceptId())
                .build();
    }

    public ConceptScrap toModel() {
        return ConceptScrap.withId(this.id, this.userId, this.conceptId);
    }
}
