package com.ripple.BE.notification.service;

import static com.ripple.BE.notification.exception.errorcode.NotificationErrorCode.*;

import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.domain.NotificationType;
import com.ripple.BE.notification.dto.NotificationDTO;
import com.ripple.BE.notification.dto.NotificationListDTO;
import com.ripple.BE.notification.exception.NotificationException;
import com.ripple.BE.notification.repository.NotificationRepository;
import com.ripple.BE.post.persistence.jpa.entity.CommentJpaEntity;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import com.ripple.BE.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseEmitterManager sseEmitterManager; // SSE 연결 관리 클래스

    private static final String POPULAR_POST_CONTENT = "이 게시글이 인기글로 선정되었습니다.";

    @Transactional
    public void createCommentNotification(
            final PostJpaEntity post, final CommentJpaEntity commentJpaEntity) {
        if (post.getAuthor() == null) {
            return;
        }

        User postAuthor = post.getAuthor();

        Notification notification =
                Notification.toNotificationEntity(
                        postAuthor,
                        commentJpaEntity.getContent(),
                        post.getTitle(),
                        NotificationType.COMMENT,
                        post);

        notificationRepository.save(notification);

        sseEmitterManager.sendNotification(postAuthor, NotificationDTO.toNotificationDTO(notification));
    }

    @Transactional
    public void createReplyNotification(
            final PostJpaEntity post, final CommentJpaEntity commentJpaEntity) {

        User postAuthor = post.getAuthor();
        User commentAuthor = commentJpaEntity.getParent().getCommenter();

        String content = commentJpaEntity.getContent();
        String title = post.getTitle();

        if (post.getAuthor() != null) {

            Notification notificationForPostAuthor =
                    Notification.toNotificationEntity(
                            postAuthor, content, title, NotificationType.REPLY, post);
            notificationRepository.save(notificationForPostAuthor);
            sseEmitterManager.sendNotification(
                    postAuthor, NotificationDTO.toNotificationDTO(notificationForPostAuthor));
        }
        if (commentJpaEntity.getCommenter() != null) {

            Notification notificationForCommentAuthor =
                    Notification.toNotificationEntity(
                            commentAuthor, content, title, NotificationType.REPLY, post);

            notificationRepository.save(notificationForCommentAuthor);

            sseEmitterManager.sendNotification(
                    commentAuthor, NotificationDTO.toNotificationDTO(notificationForCommentAuthor));
        }
    }

    @Transactional
    public void createPopularNotification(final PostJpaEntity post) {

        User receiver = post.getAuthor();
        if (receiver == null) {
            return;
        }

        Notification notification =
                Notification.toNotificationEntity(
                        receiver, POPULAR_POST_CONTENT, post.getTitle(), NotificationType.POPULAR, post);

        notificationRepository.save(notification);

        sseEmitterManager.sendNotification(receiver, NotificationDTO.toNotificationDTO(notification));
    }

    // 사용자 알림 목록 조회
    public NotificationListDTO getNotifications(final long userId) {
        return NotificationListDTO.toNotificationListDTO(notificationRepository.findByUserId(userId));
    }

    // 읽지 않은 알림 개수 조회
    public long getUnreadNotificationCount(final long receiverId) {
        return notificationRepository.countByUserIdAndIsReadFalse(receiverId);
    }

    // 알림 읽음 처리
    @Transactional
    public void markAsRead(final long notificationId) {
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() -> new NotificationException(NOTIFICATION_NOT_FOUND));
        notification.setRead(true);
    }

    // 알림 삭제
    @Transactional
    public void deleteNotification(final long notificationId) {
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() -> new NotificationException(NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
    }
}
