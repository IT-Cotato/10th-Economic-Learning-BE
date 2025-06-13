package com.ripple.BE.notification.persistence.jpa.entity;

import com.ripple.BE.global.entity.BaseJpaEntity;
import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.domain.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "notification")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long receiverId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    // 알림 유형 (예: 댓글, 대댓글, 좋아요 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Builder(access = AccessLevel.PRIVATE)
    private NotificationJpaEntity(
            Long id,
            Long receiverId,
            Long postId,
            String title,
            String content,
            NotificationType type,
            boolean isRead) {
        this.id = id;
        this.receiverId = receiverId;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
    }

    public static NotificationJpaEntity from(final Notification notification) {

        return NotificationJpaEntity.builder()
                .id(notification.getId())
                .receiverId(notification.getReceiverId())
                .postId(notification.getPostId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.isRead())
                .build();
    }

    public Notification toModel() {
        return Notification.withId(
                id, receiverId, postId, title, content, type, getCreatedDate(), isRead);
    }
}
