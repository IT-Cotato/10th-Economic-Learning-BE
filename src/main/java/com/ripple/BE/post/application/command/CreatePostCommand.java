package com.ripple.BE.post.application.command;

import com.ripple.BE.post.domain.type.PostType;
import java.util.List;

public record CreatePostCommand(
        long authorId, String title, String content, PostType type, List<Long> imageIds) {

    public static CreatePostCommand of(
            final long authorId,
            final String title,
            final String content,
            final PostType type,
            final List<Long> imageIds) {
        return new CreatePostCommand(authorId, title, content, type, imageIds);
    }
}
