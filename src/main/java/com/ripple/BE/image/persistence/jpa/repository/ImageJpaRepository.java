package com.ripple.BE.image.persistence.jpa.repository;

import com.ripple.BE.image.persistence.jpa.entity.ImageJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageJpaEntity, Long> {

    Optional<ImageJpaEntity> findByS3InfoUrl(String url);

    List<ImageJpaEntity> findByPostId(Long postId);
}
