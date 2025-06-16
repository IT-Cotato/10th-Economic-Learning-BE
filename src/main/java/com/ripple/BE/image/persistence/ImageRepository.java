package com.ripple.BE.image.persistence;

import com.ripple.BE.image.domain.Image;
import java.util.List;
import java.util.Optional;

public interface ImageRepository {

    Optional<Image> findByS3InfoUrl(String url);

    List<Image> findByPostId(Long postId);

    Image save(Image image);

    Optional<Image> findById(Long id);

    void delete(Image image);

    void saveAll(List<Image> images);

    void deleteAll(List<Image> images);
}
