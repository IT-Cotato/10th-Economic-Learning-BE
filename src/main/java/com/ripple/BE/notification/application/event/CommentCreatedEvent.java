package com.ripple.BE.notification.application.event;

import com.ripple.BE.post.domain.comment.Comment;
import com.ripple.BE.post.domain.post.Post;

public record CommentCreatedEvent(Post post, Comment comment) {}
