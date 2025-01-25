package com.ripple.BE.term.service;

import static com.ripple.BE.term.exception.errorcode.TermErrorCode.*;

import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.domain.TermScrap;
import com.ripple.BE.term.dto.TermDTO;
import com.ripple.BE.term.dto.TermListDTO;
import com.ripple.BE.term.exception.TermException;
import com.ripple.BE.term.repository.TermRepository;
import com.ripple.BE.term.repository.TermScrapRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.service.UserService;
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

    private final UserService userService;

    private static final int PAGE_SIZE = 10;

    public TermListDTO getTermsByInitial(final int page, final String consonant) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Term> terms = termRepository.findByInitial(consonant, pageable);

        return TermListDTO.toTermListDTO(terms);
    }

    public TermListDTO getTermsByKeyword(final int page, final String keyword) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Term> terms = termRepository.findByKeyword(keyword, pageable);

        return TermListDTO.toTermListDTO(terms);
    }

    public TermDTO getTerm(final long termId) {
        Term term =
                termRepository.findById(termId).orElseThrow(() -> new TermException(TERM_NOT_FOUND));

        return TermDTO.toTermDTO(term);
    }

    @Transactional
    public void addScrapToTerm(final long termId, final long userId) {

        Term term =
                termRepository.findById(termId).orElseThrow(() -> new TermException(TERM_NOT_FOUND));
        User user = userService.findUserById(userId);

        if (termScrapRepository.existsByTermIdAndUserId(termId, userId)) {
            throw new TermException(TERM_SCRAP_ALREADY_EXIST);
        }

        TermScrap termScrap = TermScrap.toTermScrapEntity();
        termScrap.setUser(user);
        termScrap.setTerm(term);
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
