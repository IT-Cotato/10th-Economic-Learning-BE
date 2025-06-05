package com.ripple.BE.learning.persistence;

import com.ripple.BE.learning.domain.learningset.LearningSetStat;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;

public interface LearningSetStatRepository {

    List<LearningSetStat> findAll();

    Optional<LearningSetStat> findByLevel(final Level level);

    LearningSetStat save(LearningSetStat learningSetStat);
}
