package com.ripple.BE.term.domain;

import com.ripple.BE.global.entity.BaseEntity;
import com.ripple.BE.term.dto.TermDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(
        name = "terms",
        indexes = {
            @Index(name = "idx_title", columnList = "title"), // title 컬럼에 인덱스 추가
            @Index(name = "idx_initial", columnList = "initial") // initial 컬럼에 인덱스 추가
        })
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Term extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Setter
    @Column(name = "initial", nullable = false)
    private String initial;

    public static Term toTermEntity(final TermDTO termDTO) {
        return Term.builder()
                .title(termDTO.title())
                .description(termDTO.description())
                .initial(termDTO.initial())
                .build();
    }
}
