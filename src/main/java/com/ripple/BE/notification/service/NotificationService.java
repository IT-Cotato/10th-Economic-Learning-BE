package com.ripple.BE.notification.service;

import static com.ripple.BE.notification.exception.errorcode.NotificationErrorCode.*;

import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.domain.NotificationType;
import com.ripple.BE.notification.dto.NotificationDTO;
import com.ripple.BE.notification.dto.NotificationListDTO;
import com.ripple.BE.notification.exception.NotificationException;
import com.ripple.BE.notification.repository.NotificationRepository;
import com.ripple.BE.post.domain.Comment;
import com.ripple.BE.post.domain.Post;
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
    public void createCommentNotification(final Post post, final Comment comment) {

        User postAuthor = post.getAuthor();

        Notification notification =
                Notification.toNotificationEntity(
                        postAuthor, comment.getContent(), post.getTitle(), NotificationType.COMMENT, post);

        notificationRepository.save(notification);

        sendNotification(postAuthor, NotificationDTO.toNotificationDTO(notification));
    }

    @Transactional
    public void createReplyNotification(final Post post, final Comment comment) {

        User postAuthor = post.getAuthor();
        User commentAuthor = comment.getParent().getCommenter();

        Notification notificationForPostAuthor =
                Notification.toNotificationEntity(
                        postAuthor, comment.getContent(), post.getTitle(), NotificationType.REPLY, post);

        Notification notificationForCommentAuthor =
                Notification.toNotificationEntity(
                        commentAuthor, comment.getContent(), post.getTitle(), NotificationType.REPLY, post);

        notificationRepository.save(notificationForPostAuthor);
        notificationRepository.save(notificationForCommentAuthor);

        sendNotification(postAuthor, NotificationDTO.toNotificationDTO(notificationForPostAuthor));
        sendNotification(
                commentAuthor, NotificationDTO.toNotificationDTO(notificationForCommentAuthor));
    }

    @Transactional
    public void createPopularNotification(final Post post) {

        User receiver = post.getAuthor();

        Notification notification =
                Notification.toNotificationEntity(
                        receiver, POPULAR_POST_CONTENT, post.getTitle(), NotificationType.POPULAR, post);

        notificationRepository.save(notification);

        sendNotification(receiver, NotificationDTO.toNotificationDTO(notification));
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

        notificationRepository.save(notification);
    }

    // 알림 삭제
    @Transactional
    public void deleteNotification(final long notificationId) {
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() -> new NotificationException(NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
        sseEmitterManager.deleteEmitter(notification.getReceiver().getId());
    }

    private void sendNotification(final User receiver, final NotificationDTO notification) {
        if (receiver.isCoummunityAlarmAllowed()) {
            sseEmitterManager.send(receiver, notification);
        }
    }
}
