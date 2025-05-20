package com.ripple.BE.post.service;

import com.ripple.BE.post.service.command.CreatePostCommand;
import com.ripple.BE.post.service.command.UpdatePostCommand;

public interface PostCommandUseCase {

    void createPost(final CreatePostCommand createPostCommand);

    void updatePost(final UpdatePostCommand updatePostCommand);

    void deletePost(final long postId, final long userId);
}
