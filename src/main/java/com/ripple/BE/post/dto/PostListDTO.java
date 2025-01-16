package com.ripple.BE.post.dto;

import com.ripple.BE.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.Page;

public record PostListDTO(List<PostDTO> postDTOList, int totalPage, int currentPage) {

    public static PostListDTO toPostListDTO(Page<Post> postPage) {
        return new PostListDTO(
                postPage.getContent().stream().map(PostDTO::toPostDTO).toList(),
                postPage.getTotalPages(),
                postPage.getNumber());
    }

    public static PostListDTO toPostListDTO(List<Post> posts) {
        return new PostListDTO(posts.stream().map(PostDTO::toPostDTO).toList(), 1, 0);
    }
}
