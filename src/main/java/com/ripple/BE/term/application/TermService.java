package com.ripple.BE.term.application;

import static com.ripple.BE.term.exception.errorcode.TermErrorCode.*;

import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.domain.TermScrap;
import com.ripple.BE.term.dto.response.TermListResponseDTO;
import com.ripple.BE.term.dto.response.TermResponseDTO;
import com.ripple.BE.term.exception.TermException;
import com.ripple.BE.term.persistence.TermRepository;
import com.ripple.BE.term.persistence.TermScrapRepository;
import com.ripple.BE.term.persistence.dto.TermWithScrapDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class TermService {

    private final TermRepository termRepository;
    private final TermScrapRepository termScrapRepository;

    private static final int PAGE_SIZE = 10;

    public TermListResponseDTO getTermsByInitial(
            final int page, final String consonant, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<TermWithScrapDTO> terms = termRepository.findByInitial(consonant, pageable, userId);

        return TermListResponseDTO.from(terms);
    }

    public TermListResponseDTO getTermsByKeyword(
            final int page, final String keyword, final long userId) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<TermWithScrapDTO> terms = termRepository.findByKeyword(keyword, pageable, userId);

        return TermListResponseDTO.from(terms);
    }

    public TermResponseDTO getTerm(final long termId) {
        Term term =
                termRepository.findById(termId).orElseThrow(() -> new TermException(TERM_NOT_FOUND));

        return TermResponseDTO.from(term);
    }

    @Transactional
    public void addScrapToTerm(final long termId, final long userId) {
        termRepository.findById(termId).orElseThrow(() -> new TermException(TERM_NOT_FOUND));

        if (termScrapRepository.existsByTermIdAndUserId(termId, userId)) {
            throw new TermException(TERM_SCRAP_ALREADY_EXIST);
        }

        TermScrap termScrap = TermScrap.withoutId(userId, termId);
        termScrapRepository.save(termScrap);
    }

    @Transactional
    public void removeScrapFromTerm(final long termId, final long userId) {
        TermScrap termScrap =
                termScrapRepository
                        .findByTermIdAndUserId(termId, userId)
                        .orElseThrow(() -> new TermException(TERM_SCRAP_NOT_FOUND));

        termScrapRepository.delete(termScrap);
    }
}
