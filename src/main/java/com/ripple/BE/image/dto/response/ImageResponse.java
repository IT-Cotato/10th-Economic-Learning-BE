package com.ripple.BE.image.dto.response;

import com.ripple.BE.image.domain.Image;

public record ImageResponse(Long id, String url) {

    public static ImageResponse from(final Image image) {
        return new ImageResponse(image.getId(), image.getS3Info().getUrl());
    }
}
