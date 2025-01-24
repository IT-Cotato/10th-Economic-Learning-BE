package com.ripple.BE.image.dto.response;

import com.ripple.BE.image.dto.ImageDTO;

public record ImageResponse(Long id, String url) {

    public static ImageResponse toImageResponse(final Long id, final String url) {
        return new ImageResponse(id, url);
    }

    public static ImageResponse toImageResponse(final ImageDTO imageDTO) {
        return new ImageResponse(imageDTO.id(), imageDTO.url());
    }
}
