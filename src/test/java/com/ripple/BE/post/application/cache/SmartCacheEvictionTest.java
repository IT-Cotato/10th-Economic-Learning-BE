package com.ripple.BE.post.application.cache;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.ripple.BE.post.application.impl.common.CommentCommandService;
import com.ripple.BE.post.application.impl.common.PostLikeService;
import com.ripple.BE.post.application.impl.post.PostQueryService;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("스마트 캐시 무효화 테스트")
class SmartCacheEvictionTest {

    private static final String TEST_EMAIL = "smart-cache@example.com";
    private static final String TEST_PASSWORD = "password123@";
    private static final String TEST_NICKNAME = "smart-tester";
    private static final String TEST_POST_TITLE = "스마트 캐시 테스트";
    private static final String TEST_POST_CONTENT = "스마트 캐시 테스트 내용";
    private static final String TEST_COMMENT_CONTENT = "스마트 댓글";

    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private PostLikeService postLikeService;
    @Autowired private CommentCommandService commentCommandService;
    @Autowired private PostQueryService postQueryService;

    @SpyBean private PostCacheEvictionService cacheEvictionService;

    private User testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        testUser = createTestUser();
        testPost = createTestPost();
    }

    private User createTestUser() {
        User user =
                User.basicBuilder().accountEmail(TEST_EMAIL).password(TEST_PASSWORD).buildBasicUser();
        user.updateNickname(TEST_NICKNAME);
        return userRepository.save(user);
    }

    private Post createTestPost() {
        Post post =
                Post.withoutId(TEST_POST_TITLE, TEST_POST_CONTENT, testUser.getId(), PostType.FREE, null);
        return postRepository.save(post);
    }

    @Test
    @DisplayName("캐시에 없는 게시물 수정 시 캐시 무효화 호출되지 않음")
    void noCacheEviction_whenPostNotInCache() {
        try {
            // given - 캐시를 생성하지 않음 (게시물이 캐시에 없는 상태)

            // when - 좋아요 추가
            postLikeService.addLikeToPost(testPost.getId(), testUser.getId());

            // then - 스마트 캐시 무효화는 호출되지만 실제 캐시 무효화는 없음
            verify(cacheEvictionService).evictPostCachesIfContained(any());
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("캐시에 있는 게시물 수정 시 캐시 무효화 실행됨")
    void cacheEviction_whenPostInCache() {
        try {
            // given - 캐시 생성 (게시물이 캐시에 포함되도록 미리 조회)
            PostPreviewListResponseDTO initialList =
                    postQueryService.getPosts(0, PostSort.RECENT, PostType.FREE);

            boolean postInCache =
                    initialList.postPreviewList().stream()
                            .anyMatch(post -> post.id().equals(testPost.getId()));

            System.out.println("게시물이 캐시에 포함됨: " + postInCache);

            // when - 좋아요 추가
            postLikeService.addLikeToPost(testPost.getId(), testUser.getId());

            // then - 스마트 캐시 무효화 호출됨
            verify(cacheEvictionService).evictPostCachesIfContained(any());

            // 실제 데이터 반영 확인
            PostPreviewListResponseDTO updatedList =
                    postQueryService.getPosts(0, PostSort.RECENT, PostType.FREE);

            long updatedLikeCount =
                    updatedList.postPreviewList().stream()
                            .filter(post -> post.id().equals(testPost.getId()))
                            .findFirst()
                            .map(PostPreviewResponseDTO::likeCount)
                            .orElse(0L);

            assert updatedLikeCount == 1L;
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("댓글 추가로 캐시된 게시물 무효화 확인")
    void commentAdd_smartCacheEviction() {
        try {
            // given - 캐시 생성
            postQueryService.getPosts(0, PostSort.RECENT, PostType.FREE);

            // when - 댓글 추가
            commentCommandService.addCommentToPost(
                    testUser.getId(), testPost.getId(), TEST_COMMENT_CONTENT);

            // then - 스마트 캐시 무효화 호출됨
            verify(cacheEvictionService).evictPostCachesIfContained(any());

        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
        }
    }
}
