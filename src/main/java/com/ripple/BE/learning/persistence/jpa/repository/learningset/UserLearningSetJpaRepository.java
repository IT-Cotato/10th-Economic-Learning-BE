package com.ripple.BE.learning.persistence.jpa.repository.learningset;

import com.ripple.BE.learning.persistence.jpa.entity.learningset.UserLearningSetJpaEntity;
import com.ripple.BE.user.domain.type.Level;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLearningSetJpaRepository
        extends JpaRepository<UserLearningSetJpaEntity, Long> {

    Optional<UserLearningSetJpaEntity> findByUserIdAndLearningSetIdAndLevel(
            Long userId, Long learningSetId, Level level);

    @Query(
            "SELECT lsc FROM UserLearningSetJpaEntity lsc "
                    + "WHERE lsc.userId = :userId AND lsc.level = :level")
    List<UserLearningSetJpaEntity> findByUserIdAndLevel(
            @Param("userId") long userId, @Param("level") Level level);

    void deleteAllByUserId(Long userId);

    List<UserLearningSetJpaEntity> findByUserIdAndLearningSetId(Long userId, Long learningSetId);
}
