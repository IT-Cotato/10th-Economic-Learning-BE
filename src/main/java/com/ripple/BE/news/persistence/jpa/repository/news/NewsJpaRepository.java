package com.ripple.BE.news.persistence.jpa.repository.news;

import com.ripple.BE.news.persistence.jpa.entity.NewsJpaEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsJpaRepository extends JpaRepository<NewsJpaEntity, Long>, NewsQueryRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT n FROM NewsJpaEntity n WHERE n.id = :id")
    Optional<NewsJpaEntity> findByIdForUpdate(long id);
}
