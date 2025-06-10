package com.ripple.BE.term.persistence.impl;

import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.persistence.TermRepository;
import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import com.ripple.BE.term.persistence.jdbc.TermJdbcRepository;
import com.ripple.BE.term.persistence.jpa.entity.TermJpaEntity;
import com.ripple.BE.term.persistence.jpa.repository.term.TermJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TermRepositoryImpl implements TermRepository {

    private final TermJpaRepository termJpaRepository;
    private final TermJdbcRepository termJdbcRepository;

    @Override
    public Page<TermWithScrapDTO> findByInitial(String consonant, Pageable pageable, long userId) {
        return termJpaRepository.findByInitial(consonant, pageable, userId);
    }

    @Override
    public Page<TermWithScrapDTO> findByKeyword(String keyword, Pageable pageable, long userId) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return termJpaRepository.findAllWithoutKeyword(pageable, userId);
        }
        return termJdbcRepository.searchTerms(keyword, pageable, userId);
    }

    @Override
    public Page<TermWithScrapDTO> searchTerms(String keyword, Pageable pageable, long userId) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return termJpaRepository.findAllWithoutKeyword(pageable, userId);
        }
        return termJdbcRepository.searchTerms(keyword, pageable, userId);
    }

    @Override
    public Optional<Term> findById(long termId) {
        return termJpaRepository.findById(termId).map(TermJpaEntity::toModel);
    }

    @Override
    public void saveAllTerms(List<Term> terms) {
        List<TermJpaEntity> termEntities = terms.stream().map(TermJpaEntity::from).toList();

        termJdbcRepository.saveAllTermsByJdbcTemplate(termEntities);
    }

    @Override
    public List<Term> findAll() {
        return termJpaRepository.findAll().stream().map(TermJpaEntity::toModel).toList();
    }
}
