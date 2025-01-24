package com.ripple.BE.term.service;

import static com.ripple.BE.term.exception.errorcode.TermErrorCode.*;

import com.ripple.BE.term.domain.Term;
import com.ripple.BE.term.dto.TermDTO;
import com.ripple.BE.term.dto.TermListDTO;
import com.ripple.BE.term.exception.TermException;
import com.ripple.BE.term.repository.TermRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class TermService {

    private final TermRepository termRepository;

    public TermListDTO getTermsByInitial(final String consonant) {

        List<Term> terms = termRepository.findByInitial(consonant);

        return TermListDTO.toTermListDTO(terms);
    }

    public TermListDTO getTermsByKeyword(final String keyword) {
        List<Term> terms = termRepository.findByKeyword(keyword);

        return TermListDTO.toTermListDTO(terms);
    }

    public TermDTO getTerm(final long termId) {
        Term term =
                termRepository.findById(termId).orElseThrow(() -> new TermException(TERM_NOT_FOUND));

        return TermDTO.toTermDTO(term);
    }
}
