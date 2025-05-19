package com.ripple.BE.global.entity;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class BaseEntity {

    private final LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    protected BaseEntity() {
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    public void touchModifiedDate() {
        this.modifiedDate = LocalDateTime.now();
    }
}
