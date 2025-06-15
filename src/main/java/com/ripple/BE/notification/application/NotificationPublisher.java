package com.ripple.BE.notification.application;

import com.ripple.BE.notification.application.event.CommentCreatedEvent;
import com.ripple.BE.notification.application.event.ReplyCommentCreatedEvent;
import com.ripple.BE.notification.application.event.SelectedPopularPostEvent;
import com.ripple.BE.notification.application.redis.RedisNotificationPublisher;
import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.domain.NotificationType;
import com.ripple.BE.notification.dto.NotificationSseDTO;
import com.ripple.BE.notification.persistence.NotificationRepository;
import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class NotificationPublisher {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final RedisNotificationPublisher redisNotificationPublisher;

    private static final String POPULAR_POST_CONTENT = "이 게시글이 인기글로 선정되었습니다.";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createCommentNotification(final CommentCreatedEvent event) {
        Post post = event.post();
        Comment comment = event.comment();

        if (post.getAuthorId() == null) {
            return;
        }

        User postAuthor = userRepository.findById(post.getAuthorId()).orElse(null);

        if (postAuthor != null
                && postAuthor.isCoummunityAlarmAllowed()
                && !Objects.equals(postAuthor.getId(), comment.getCommenterId())) {

            Notification notification =
                    Notification.withoutId(
                            post.getAuthorId(),
                            post.getId(),
                            post.getTitle(),
                            comment.getContent(),
                            NotificationType.COMMENT);

            saveAndPublish(notification);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createReplyNotification(final ReplyCommentCreatedEvent event) {
        Post post = event.post();
        Comment parentComment = event.parentComment();
        Comment comment = event.replyComment();

        if (post.getAuthorId() == null || parentComment.getParentCommentId() == null) {
            return;
        }

        User postAuthor = userRepository.findById(post.getAuthorId()).orElse(null);
        User parentCommentAuthor = userRepository.findById(parentComment.getCommenterId()).orElse(null);

        if (postAuthor != null
                && postAuthor.isCoummunityAlarmAllowed()
                && !Objects.equals(postAuthor.getId(), comment.getCommenterId())) {
            Notification notificationForPostAuthor =
                    Notification.withoutId(
                            postAuthor.getId(),
                            post.getId(),
                            post.getTitle(),
                            comment.getContent(),
                            NotificationType.COMMENT);

            saveAndPublish(notificationForPostAuthor);
        }

        if (parentCommentAuthor != null
                && parentCommentAuthor.isCoummunityAlarmAllowed()
                && !Objects.equals(parentCommentAuthor.getId(), comment.getCommenterId())) {
            Notification notificationForCommentAuthor =
                    Notification.withoutId(
                            parentCommentAuthor.getId(),
                            post.getId(),
                            post.getTitle(),
                            comment.getContent(),
                            NotificationType.REPLY);

            saveAndPublish(notificationForCommentAuthor);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createPopularNotification(final SelectedPopularPostEvent event) {
        Post post = event.post();

        if (post.getAuthorId() == null) {
            return;
        }

        User receiver = userRepository.findById(post.getAuthorId()).orElse(null);

        if (receiver != null && receiver.isCoummunityAlarmAllowed()) {

            // 인기 게시글 알림 생성
            Notification notification =
                    Notification.withoutId(
                            receiver.getId(),
                            post.getId(),
                            post.getTitle(),
                            POPULAR_POST_CONTENT,
                            NotificationType.POPULAR);

            saveAndPublish(notification);
        }
    }

    private void saveAndPublish(final Notification notification) {
        // 알림 저장
        notificationRepository.save(notification);
        try {
            redisNotificationPublisher.publishNotification(NotificationSseDTO.from(notification));
        } catch (Exception e) {
            log.error("Failed to publish notification to Redis: {}", e.getMessage());
        }
    }
}
