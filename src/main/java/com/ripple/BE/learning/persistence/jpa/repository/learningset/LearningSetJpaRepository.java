package com.ripple.BE.learning.persistence.jpa.repository.learningset;

import com.ripple.BE.learning.persistence.jpa.entity.learningset.LearningSetJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningSetJpaRepository extends JpaRepository<LearningSetJpaEntity, Long> {

    @Query("SELECT l.name FROM LearningSetJpaEntity l WHERE l.id = :id")
    Optional<String> findNameById(@Param("id") Long id);
}
