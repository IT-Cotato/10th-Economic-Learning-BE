package com.ripple.BE.image.persistence.jpa.entity;

import com.ripple.BE.image.domain.S3Info;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class S3InfoJpaEntity {
    private String folderName;
    private String fileName;
    private String url;

    @Builder(access = AccessLevel.PRIVATE)
    public S3InfoJpaEntity(String folderName, String fileName, String url) {
        this.folderName = folderName;
        this.fileName = fileName;
        this.url = url;
    }

    public static S3InfoJpaEntity from(S3Info s3Info) {
        return S3InfoJpaEntity.builder()
                .folderName(s3Info.getFolderName())
                .fileName(s3Info.getFileName())
                .url(s3Info.getUrl())
                .build();
    }

    public S3Info toModel() {
        return S3Info.of(this.folderName, this.fileName, this.url);
    }
}
