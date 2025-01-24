package com.ripple.BE.news.repository.news;

import com.ripple.BE.news.domain.News;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT n FROM News n WHERE n.id = :id")
    Optional<News> findByIdForUpdate(long id);
}
