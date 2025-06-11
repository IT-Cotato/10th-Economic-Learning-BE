package com.ripple.BE.term.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.term.domain.TermScrap;
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

@Table(name = "term_scraps")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TermScrapJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "term_id")
    private Long termId;

    @Builder(access = AccessLevel.PRIVATE)
    private TermScrapJpaEntity(Long id, Long userId, Long termId) {
        this.id = id;
        this.userId = userId;
        this.termId = termId;
    }

    public static TermScrapJpaEntity from(TermScrap termScrap) {
        return TermScrapJpaEntity.builder()
                .id(termScrap.getId())
                .userId(termScrap.getUserId())
                .termId(termScrap.getTermId())
                .build();
    }

    public TermScrap toModel() {
        return TermScrap.withId(id, userId, termId);
    }
}
