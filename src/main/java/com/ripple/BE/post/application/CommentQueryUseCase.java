package com.ripple.BE.post.application;

import com.ripple.BE.post.dto.response.CommentResponseDTO;
import java.util.List;

public interface CommentQueryUseCase {

    List<CommentResponseDTO> getComments(long postId, long userId);
}
