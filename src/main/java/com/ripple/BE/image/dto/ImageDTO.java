package com.ripple.BE.image.dto;

import com.ripple.BE.image.domain.Image;

public record ImageDTO(String url) {

    public static ImageDTO toImageDTO(final Image image) {
        return new ImageDTO(image.getImageUrl());
    }

    public static ImageDTO toImageDTO(final String imageUrl) {
        return new ImageDTO(imageUrl);
    }
}
