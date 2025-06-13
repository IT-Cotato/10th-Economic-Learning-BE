package com.ripple.BE.notification.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Notification {

    private final Long id;

    private final Long receiverId; // 수신자 ID
    private final Long postId; // 게시글 ID (알림이 게시글과 관련된 경우)
    private final String title; // 알림 제목
    private final String content; // 알림 내용
    private final NotificationType type; // 알림 유형 (예: 댓글, 대댓글, 좋아요 등)
    private final boolean isRead; // 알림 읽음 여부
    private final LocalDateTime createdDate; // 알림 생성 날짜

    @Builder(access = AccessLevel.PRIVATE)
    private Notification(
            Long id,
            Long receiverId,
            Long postId,
            String title,
            String content,
            NotificationType type,
            LocalDateTime createdDate,
            boolean isRead) {
        this.id = id;
        this.receiverId = receiverId;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
        this.createdDate = createdDate;
    }

    public static Notification withId(
            Long id,
            Long receiverId,
            Long postId,
            String title,
            String content,
            NotificationType type,
            LocalDateTime createdDate,
            boolean isRead) {
        return Notification.builder()
                .id(id)
                .receiverId(receiverId)
                .postId(postId)
                .title(title)
                .content(content)
                .type(type)
                .createdDate(createdDate)
                .isRead(isRead)
                .build();
    }

    public static Notification withoutId(
            Long receiverId, Long postId, String title, String content, NotificationType type) {
        return Notification.builder()
                .receiverId(receiverId)
                .postId(postId)
                .title(title)
                .content(content)
                .type(type)
                .createdDate(LocalDateTime.now())
                .isRead(false)
                .build();
    }

    public Notification updateIsRead(boolean isRead) {
        return Notification.builder()
                .id(this.id)
                .receiverId(this.receiverId)
                .postId(this.postId)
                .title(this.title)
                .content(this.content)
                .type(this.type)
                .createdDate(this.createdDate)
                .isRead(isRead)
                .build();
    }
}
