package com.ripple.BE.notification;

import static org.assertj.core.api.Assertions.assertThat;

import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.persistence.NotificationRepository;
import com.ripple.BE.post.application.impl.common.CommentCommandService;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.impl.post.CommentRepositoryImpl;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.repository.UserRepository;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class EventListenerTest {

    private static final Logger log = LoggerFactory.getLogger(EventListenerTest.class);

    @Autowired private UserRepository userRepository;

    @Autowired private PostRepository postRepository;

    @Autowired private NotificationRepository notificationRepository;

    @Autowired private CommentCommandService commentCommandService;

    private User user1;
    private Post post1;
    @Autowired private CommentRepositoryImpl commentRepositoryImpl;

    @BeforeEach
    void setUp() {
        user1 =
                User.basicBuilder().accountEmail("user1@test.com").password("pw12345678@").buildBasicUser();
        user1.updateNickname("user1");
        user1 = userRepository.save(user1);

        post1 = Post.withoutId("제목1", "내용1", user1.getId(), PostType.FREE, null);
        post1 = postRepository.save(post1);
    }

    @Test
    @DisplayName("댓글이 성공적으로 저장되고, 알림이 성공적으로 생성되어야 한다")
    void commentCreatedEventTest() {
        // given
        String content = "댓글 내용";
        long postId = post1.getId();
        long userId = user1.getId();

        // when
        try {
            commentCommandService.addCommentToPost(userId, postId, content);
        } catch (Exception e) {
            log.error("예외 발생: {}", e.getMessage());
        }

        // then
        Notification notification =
                notificationRepository.findByUserIdOrderByCreatedDateDesc(userId).stream()
                        .filter(n -> n.getPostId() == postId)
                        .findFirst()
                        .orElse(null);

        assertThat(notification).isNotNull();

        log.info("생성된 알림: {}", notification);
    }
}
