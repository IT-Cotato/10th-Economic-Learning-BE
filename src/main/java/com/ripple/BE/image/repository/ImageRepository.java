package com.ripple.BE.image.repository;

import com.ripple.BE.image.domain.Image;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByS3InfoUrl(String url);

    List<Image> findByPostId(Long postId);
}
