package com.ripple.BE.post.repository.post;

import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostType;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.id = :id")
    Optional<Post> findByIdForUpdate(long id);

    List<Post> findByIdIn(List<Long> ids);

    @Query("SELECT p.title FROM Post p WHERE p.type = :type")
    Set<String> findExistingTitlesByType(@Param("type") PostType type);
}
