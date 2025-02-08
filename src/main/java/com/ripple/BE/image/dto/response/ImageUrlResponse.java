package com.ripple.BE.image.dto.response;

public record ImageUrlResponse(String imageUrl) {
    public static ImageUrlResponse toImageUrlResponse(String imageUrl) {
        return new ImageUrlResponse(imageUrl);
    }
}
