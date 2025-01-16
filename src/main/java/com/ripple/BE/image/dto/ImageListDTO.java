package com.ripple.BE.image.dto;

import com.ripple.BE.image.domain.Image;
import java.util.List;

public record ImageListDTO(List<ImageDTO> imageDTOList) {

    public static ImageListDTO toImageListDTO(List<Image> imageList) {
        return new ImageListDTO(imageList.stream().map(ImageDTO::toImageDTO).toList());
    }
}
