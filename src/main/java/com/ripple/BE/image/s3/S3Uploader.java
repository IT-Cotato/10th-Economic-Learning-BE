package com.ripple.BE.image.s3;

import static com.ripple.BE.image.exception.errorcode.ImageErrorCode.*;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ripple.BE.image.domain.S3Info;
import com.ripple.BE.image.exception.ImageException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private static final String CONTENT_TYPE = "multipart/formed-data";
    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Info uploadFiles(MultipartFile multipartFile, String folderName) throws ImageException {
        File localUploadFile = convertToFile(multipartFile);
        return uploadFileToS3(localUploadFile, folderName);
    }

    public S3Info uploadFileToS3(File file, String folderName) {
        String fileName = buildFileName(folderName, file.getName());
        String uploadUrl = uploadToS3(file, fileName);

        file.delete();

        return buildS3Info(folderName, file, uploadUrl);
    }

    public void deleteFile(S3Info s3Info) {
        String fileName = buildFileName(s3Info.getFolderName(), s3Info.getFileName());
        log.info("{} 사진 삭제", fileName);
        amazonS3.deleteObject(bucket, fileName);
    }

    public boolean isValidS3Url(String folderName, String imageUrl) {
        try {
            // S3에서 해당 URL의 객체가 존재하는지 확인, 입력 예시 : "toktok/1234.jpg"
            String fileName = folderName + "/" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            return amazonS3.doesObjectExist(bucket, fileName);
        } catch (Exception e) {
            log.error("S3 URL 검증 중 오류 발생: {}", imageUrl, e);
            return false;
        }
    }

    private String uploadToS3(File uploadFile, String fileName) {
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(CONTENT_TYPE);
        amazonS3.putObject(putObjectRequest);

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private static File convertToFile(MultipartFile file) throws ImageException {
        String fileExtension = getFileExtension(file);
        File convertedFile =
                new File(System.getProperty("user.dir") + "/" + UUID.randomUUID() + "." + fileExtension);

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new ImageException(IMAGE_PROCESSING_FAIL);
        }

        return convertedFile;
    }

    private static String getFileExtension(MultipartFile file) throws ImageException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new ImageException(IMAGE_NOT_FOUND);
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    private String buildFileName(String folderName, String fileName) {
        return folderName + "/" + fileName;
    }

    private S3Info buildS3Info(String folderName, File file, String uploadUrl) {
        return S3Info.builder().folderName(folderName).fileName(file.getName()).url(uploadUrl).build();
    }
}
