package com.ripple.BE.image.service;

import static com.ripple.BE.image.exception.errorcode.ImageErrorCode.*;

import com.ripple.BE.image.domain.Image;
import com.ripple.BE.image.domain.S3Info;
import com.ripple.BE.image.exception.ImageException;
import com.ripple.BE.image.repository.ImageRepository;
import com.ripple.BE.image.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    private static final String FOLDER_NAME = "post";

    @Transactional
    public long addImageToPost(MultipartFile file) {

        S3Info s3Info = s3Uploader.uploadFiles(file, FOLDER_NAME);

        Image image = imageRepository.save(Image.toImageEntity(s3Info));
        return image.getId();
    }

    @Transactional
    public void deleteImageFromPost(long imageId) {
        Image image =
                imageRepository.findById(imageId).orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));
        s3Uploader.deleteFile(image.getS3Info());

        imageRepository.delete(image);
    }
}
