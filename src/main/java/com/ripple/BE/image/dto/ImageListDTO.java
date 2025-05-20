package com.ripple.BE.image.dto;

import com.ripple.BE.image.domain.Image;
import java.util.List;

public record ImageListDTO(List<ImageDTO> imageDTOList) {

    public static ImageListDTO toImageListDTO(List<Image> imageList) {
        if (imageList == null) {
            return new ImageListDTO(List.of()); // 또는 Collections.emptyList()
        }
        return new ImageListDTO(imageList.stream().map(ImageDTO::toImageDTO).toList());
    }
}
