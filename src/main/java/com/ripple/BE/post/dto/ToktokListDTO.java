package com.ripple.BE.post.dto;

import com.ripple.BE.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.Page;

public record ToktokListDTO(List<ToktokDTO> toktokDTOList, int totalPage, int currentPage) {

    public static ToktokListDTO toToktokListDTO(Page<Post> postPage) {
        return new ToktokListDTO(
                postPage.getContent().stream().map(ToktokDTO::toToktokDTO).toList(),
                postPage.getTotalPages(),
                postPage.getNumber());
    }
}
