package com.ripple.BE.image.persistence.impl;

import com.ripple.BE.image.domain.Image;
import com.ripple.BE.image.persistence.ImageRepository;
import com.ripple.BE.image.persistence.jpa.entity.ImageJpaEntity;
import com.ripple.BE.image.persistence.jpa.repository.ImageJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public Image save(Image image) {
        return imageJpaRepository.save(ImageJpaEntity.from(image)).toModel();
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageJpaRepository.findById(id).map(ImageJpaEntity::toModel);
    }

    @Override
    public Optional<Image> findByS3InfoUrl(String url) {
        return imageJpaRepository.findByS3InfoUrl(url).map(ImageJpaEntity::toModel);
    }

    @Override
    public void delete(Image image) {
        imageJpaRepository.delete(ImageJpaEntity.from(image));
    }

    @Override
    public List<Image> findByPostId(Long postId) {
        return imageJpaRepository.findByPostId(postId).stream().map(ImageJpaEntity::toModel).toList();
    }

    @Override
    public void saveAll(List<Image> images) {
        List<ImageJpaEntity> imageEntities = images.stream().map(ImageJpaEntity::from).toList();
        imageJpaRepository.saveAll(imageEntities);
    }

    @Override
    public void deleteAll(List<Image> images) {
        List<ImageJpaEntity> imageEntities = images.stream().map(ImageJpaEntity::from).toList();
        imageJpaRepository.deleteAll(imageEntities);
    }
}
