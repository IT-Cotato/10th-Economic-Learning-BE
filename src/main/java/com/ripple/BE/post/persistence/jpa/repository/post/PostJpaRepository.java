package com.ripple.BE.post.persistence.jpa.repository.post;

import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface PostJpaRepository
        extends JpaRepository<PostJpaEntity, Long>, PostQueryRepository, ToktokQueryRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PostJpaEntity p WHERE p.id = :id")
    Optional<PostJpaEntity> findByIdForUpdate(long id);

    List<PostJpaEntity> findAllByAuthorId(long authorId);

    void deleteAllByAuthorId(long authorId);
}
