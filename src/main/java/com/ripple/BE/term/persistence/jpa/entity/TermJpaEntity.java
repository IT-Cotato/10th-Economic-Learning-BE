package com.ripple.BE.term.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.term.domain.Term;
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

@Table(name = "terms")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TermJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "initial", nullable = false)
    private String initial;

    @Builder(access = AccessLevel.PRIVATE)
    private TermJpaEntity(Long id, String title, String description, String initial) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.initial = initial;
    }

    public static TermJpaEntity from(Term term) {
        return TermJpaEntity.builder()
                .id(term.getId())
                .title(term.getTitle())
                .description(term.getDescription())
                .initial(term.getInitial())
                .build();
    }

    public Term toModel() {
        return Term.withId(id, title, description, initial);
    }
}
