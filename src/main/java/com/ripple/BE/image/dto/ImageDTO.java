package com.ripple.BE.image.dto;

import com.ripple.BE.image.domain.Image;

public record ImageDTO(Long id, String url) {

    public static ImageDTO toImageDTO(final Image image) {
        return new ImageDTO(image.getId(), image.getS3Info().getUrl());
    }

    public static ImageDTO toImageDTO(final String imageUrl) {
        return new ImageDTO(null, imageUrl);
    }
}
