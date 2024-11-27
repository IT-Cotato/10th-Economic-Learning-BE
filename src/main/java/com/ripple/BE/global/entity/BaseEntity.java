package com.ripple.BE.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // 이 클래스를 직접 테이블로 매핑하지 않고, 자식 클래스에 매핑 정보를 전달하도록 설정
@EntityListeners(
        AuditingEntityListener.class) // JPA 엔티티의 상태 변화를 감지하여 Auditing(자동 필드 값 설정)을 수행하는 리스너를 추가
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;
}
