package com.ripple.BE.term.persistence.jpa.repository.termscrap;

import com.ripple.BE.term.persistence.jpa.entity.TermScrapJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermScrapJpaRepository
        extends JpaRepository<TermScrapJpaEntity, Long>, TermScrapQueryRepository {

    Optional<TermScrapJpaEntity> findByTermIdAndUserId(long termId, long userId);

    boolean existsByTermIdAndUserId(long termId, long userId);

    void deleteAllByUserId(long userId);
}
