package com.ripple.BE.learning.repository;

import com.ripple.BE.learning.domain.UserLearningSet;
import com.ripple.BE.user.domain.type.Level;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLearningSetRepository extends JpaRepository<UserLearningSet, Long> {

    Optional<UserLearningSet> findByUserIdAndLearningSetIdAndLevel(
            Long userId, Long learningSetId, Level level);

    @Query(
            "SELECT lsc FROM UserLearningSet lsc "
                    + "JOIN FETCH lsc.learningSet ls "
                    + "JOIN FETCH lsc.user u "
                    + "WHERE u.id = :userId"
                    + " AND lsc.level = :level")
    List<UserLearningSet> findByUserIdAndLevel(
            @Param("userId") long userId, @Param("level") Level level);
}
