package com.ripple.BE.post.service;

import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.domain.type.PostType;
import com.ripple.BE.post.dto.response.PostPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.PostPreviewResponseDTO;
import com.ripple.BE.post.dto.response.PostResponseDTO;
import java.util.List;

public interface PostQueryUseCase {

    PostPreviewListResponseDTO getPosts(
            final int page, final PostSort sort, final PostType type, final long userId);

    List<PostPreviewResponseDTO> getPopularPosts(final long userId);

    PostResponseDTO getPost(final long postId, final long userId);

    PostPreviewListResponseDTO searchPosts(final String keyword, final int page, final long userId);
}
