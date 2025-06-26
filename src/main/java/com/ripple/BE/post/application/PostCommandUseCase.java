package com.ripple.BE.post.application;

import com.ripple.BE.post.application.command.CreatePostCommand;
import com.ripple.BE.post.application.command.UpdatePostCommand;

public interface PostCommandUseCase {

    void createPost(final CreatePostCommand createPostCommand);

    void updatePost(final UpdatePostCommand updatePostCommand);

    void deletePost(final long postId, final long userId);

    void deleteAllPostsByUserId(final long userId);
}
