package com.ripple.BE.image.dto.response;

public record ImageIdResponse(Long imageId) {
    public static ImageIdResponse toImageIdResponse(long imageId) {
        return new ImageIdResponse(imageId);
    }
}
