package com.ripple.BE.term.repository;

import com.ripple.BE.term.domain.TermScrap;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermScrapRepository extends JpaRepository<TermScrap, Long> {

    Optional<TermScrap> findByTermIdAndUserId(long termId, long userId);

    boolean existsByTermIdAndUserId(long termId, long userId);
}
