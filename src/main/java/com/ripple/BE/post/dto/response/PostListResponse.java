package com.ripple.BE.post.dto.response;

import com.ripple.BE.post.dto.PostListDTO;
import java.util.List;

public record PostListResponse(List<PostPreviewResponse> postList, int totalPage, int currentPage) {

    public static PostListResponse toPostListResponse(PostListDTO postListDTO) {
        return new PostListResponse(
                postListDTO.postDTOList().stream().map(PostPreviewResponse::toPostPreviewResponse).toList(),
                postListDTO.totalPage(),
                postListDTO.currentPage());
    }
}
