package com.ripple.BE.post.service.command;

import com.ripple.BE.post.domain.type.PostType;
import java.util.List;

public record UpdatePostCommand(
        long postId,
        long authorId,
        String newTitle,
        String newContent,
        PostType newType,
        List<Long> newImageIds) {

    public static UpdatePostCommand of(
            final long postId,
            final long authorId,
            final String newTitle,
            final String newContent,
            final PostType newType,
            final List<Long> newImageIds) {
        return new UpdatePostCommand(postId, authorId, newTitle, newContent, newType, newImageIds);
    }
}
