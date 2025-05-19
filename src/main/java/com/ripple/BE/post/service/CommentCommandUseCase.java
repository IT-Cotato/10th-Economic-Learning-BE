package com.ripple.BE.post.service;

public interface CommentCommandUseCase {

    void addCommentToPost(final long userId, final long postId, final String content);

    void addReplyToComment(
            final long userId, final long postId, final long commentId, final String content);

    void removeCommentFromPost(final long userId, final long postId, final long commentId);

    void updateComment(
            final long userId, final long postId, final long commentId, final String content);
}
