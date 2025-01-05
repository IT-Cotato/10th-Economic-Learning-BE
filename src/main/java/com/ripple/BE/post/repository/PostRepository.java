package com.ripple.BE.post.repository;

import com.ripple.BE.post.domain.Post;
import com.ripple.BE.post.domain.type.PostType;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.id = :id")
    Optional<Post> findByIdForUpdate(long id);

    Page<Post> findByType(PostType type, Pageable pageable);

    List<Post> findByTypeAndUsedDateIsNull(PostType type);

    Page<Post> findByTypeAndUsedDateIsNotNull(PostType type, Pageable pageable);

    Optional<Post> findByTypeAndUsedDate(PostType type, LocalDate usedDate);
}
