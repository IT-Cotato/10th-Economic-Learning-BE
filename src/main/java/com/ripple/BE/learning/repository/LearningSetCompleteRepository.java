package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.LearningSetComplete;
import com.ripple.BE.user.domain.type.Level;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningSetCompleteRepository extends JpaRepository<LearningSetComplete, Long> {

    Optional<LearningSetComplete> findByUserIdAndLearningSetId(Long userId, Long learningSetId);

    @Query(
            "SELECT lsc FROM LearningSetComplete lsc "
                    + "JOIN FETCH lsc.learningSet ls "
                    + "JOIN FETCH lsc.user u "
                    + "WHERE u.id = :userId AND ls.level = :level")
    List<LearningSetComplete> findByUserIdAndLearningSetLevel(
            @Param("userId") long userId, @Param("level") Level level);

    @Query(
            "SELECT l.learningSet.id FROM LearningSetComplete l WHERE l.user.id = :userId AND l.isLearningSetCompleted = true")
    List<Long> findCompletedLearningSetIdsByUserId(@Param("userId") Long userId);
}
