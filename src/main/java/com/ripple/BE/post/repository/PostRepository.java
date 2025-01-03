package com.ripple.BE.post.repository;

import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostType;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.id = :id")
    Optional<Post> findByIdForUpdate(long id);

    // 특정 타입의 게시글 중 아직 선정되지 않은 게시글 조회
    @Query(
            "SELECT p FROM Post p WHERE p.type = :type AND p.id NOT IN (SELECT t.post.id FROM TodayToktok t)")
    List<Post> findUnselectedPosts(@Param("type") PostType type);
}
