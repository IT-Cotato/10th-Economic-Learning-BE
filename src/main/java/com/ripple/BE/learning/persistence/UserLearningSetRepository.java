package com.ripple.BE.learning.persistence;

import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;

public interface UserLearningSetRepository {

    Optional<UserLearningSet> findByUserIdAndLearningSetIdAndLevel(
            final long userId, final long learningSetId, final Level level);

    List<UserLearningSet> findByUserIdAndLevel(final long userId, final Level level);

    List<UserLearningSet> saveAll(final List<UserLearningSet> userLearningSets);

    UserLearningSet save(final UserLearningSet userLearningSet);

    List<UserLearningSet> findAll();

    void deleteAllByUserId(final long userId);
}
