package com.ripple.BE.post.persistence;

import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.persistence.dto.ToktokWithImageDTO;
import com.ripple.BE.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ToktokRepository {

    Post save(final Post toktokPost);

    void delete(final Post toktokPost);

    // 게시글에 댓글을 단 사용자 목록을 조회한다.
    List<User> findUsersByToktokPostId(final long postId);

    Optional<ToktokWithImageDTO> findByUsedDate(final LocalDate usedDate);

    Optional<Post> findById(final long id);

    Page<ToktokWithImageDTO> findUsedToktokPosts(final Pageable pageable, final PostSort postSort);

    List<Post> findNewToktokPosts();

    Set<String> findAllTitles();

    Page<ToktokWithImageDTO> searchUsedToktokPosts(final String keyword, final Pageable pageable);

    Map<Long, List<User>> findUsersByToktokPostIds(List<Long> postIds);
}
