package com.ripple.BE.learning.persistence.impl;

import com.ripple.BE.learning.domain.learningset.UserLearningSet;
import com.ripple.BE.learning.persistence.UserLearningSetRepository;
import com.ripple.BE.learning.persistence.jpa.entity.learningset.UserLearningSetJpaEntity;
import com.ripple.BE.learning.persistence.jpa.repository.learningset.UserLearningSetJpaRepository;
import com.ripple.BE.user.domain.type.Level;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserLearningSetRepositoryImpl implements UserLearningSetRepository {

    private final UserLearningSetJpaRepository userLearningSetJpaRepository;

    @Override
    public Optional<UserLearningSet> findByUserIdAndLearningSetIdAndLevel(
            final long userId, final long learningSetId, final Level level) {
        return userLearningSetJpaRepository
                .findByUserIdAndLearningSetIdAndLevel(userId, learningSetId, level)
                .map(UserLearningSetJpaEntity::toModel);
    }

    @Override
    public List<UserLearningSet> findByUserIdAndLevel(final long userId, final Level level) {
        return userLearningSetJpaRepository.findByUserIdAndLevel(userId, level).stream()
                .map(UserLearningSetJpaEntity::toModel)
                .toList();
    }

    @Override
    public List<UserLearningSet> saveAll(final List<UserLearningSet> userLearningSets) {

        return userLearningSetJpaRepository
                .saveAll(userLearningSets.stream().map(UserLearningSetJpaEntity::from).toList())
                .stream()
                .map(UserLearningSetJpaEntity::toModel)
                .toList();
    }

    @Override
    public UserLearningSet save(final UserLearningSet userLearningSet) {
        return userLearningSetJpaRepository
                .save(UserLearningSetJpaEntity.from(userLearningSet))
                .toModel();
    }

    @Override
    public List<UserLearningSet> findAll() {
        return userLearningSetJpaRepository.findAll().stream()
                .map(UserLearningSetJpaEntity::toModel)
                .toList();
    }

    @Override
    public void deleteAllByUserId(final long userId) {
        userLearningSetJpaRepository.deleteAllByUserId(userId);
    }

    @Override
    public List<UserLearningSet> findByUserIdAndLearningSetId(long userId, long learningSetId) {
        return userLearningSetJpaRepository.findByUserIdAndLearningSetId(userId, learningSetId).stream()
                .map(UserLearningSetJpaEntity::toModel)
                .toList();
    }
}
