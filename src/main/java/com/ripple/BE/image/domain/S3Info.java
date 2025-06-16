package com.ripple.BE.image.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class S3Info {
    private final String folderName;
    private final String fileName;
    private final String url;

    @Builder(access = AccessLevel.PRIVATE)
    public S3Info(String folderName, String fileName, String url) {
        this.folderName = folderName;
        this.fileName = fileName;
        this.url = url;
    }

    public static S3Info of(String folderName, String fileName, String url) {
        return S3Info.builder().folderName(folderName).fileName(fileName).url(url).build();
    }
}
