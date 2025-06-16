package com.ripple.BE.post.application.impl.post;

import static com.ripple.BE.image.exception.errorcode.ImageErrorCode.*;
import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.image.domain.Image;
import com.ripple.BE.image.exception.ImageException;
import com.ripple.BE.image.persistence.ImageRepository;
import com.ripple.BE.post.application.PostCommandUseCase;
import com.ripple.BE.post.application.command.CreatePostCommand;
import com.ripple.BE.post.application.command.UpdatePostCommand;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.PostLikeRepository;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.PostScrapRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandService implements PostCommandUseCase {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public void createPost(final CreatePostCommand createPostCommand) {
        Post post =
                Post.withoutId(
                        createPostCommand.title(),
                        createPostCommand.content(),
                        createPostCommand.authorId(),
                        createPostCommand.type(),
                        null);

        Post saved = postRepository.save(post);

        if (createPostCommand.imageIds() != null && !createPostCommand.imageIds().isEmpty()) {
            List<Image> imagesToUpdate = new ArrayList<>();

            for (long imageId : createPostCommand.imageIds()) {
                Image image =
                        imageRepository
                                .findById(imageId)
                                .orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));
                imagesToUpdate.add(image.updatePostId(saved.getId()));
            }

            imageRepository.saveAll(imagesToUpdate);
        }
    }

    @Override
    public void updatePost(final UpdatePostCommand updatePostCommand) {
        Post post =
                postRepository
                        .findById(updatePostCommand.postId())
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (!post.isOwnedBy(updatePostCommand.authorId())) {
            throw new PostException(POST_NOT_AUTHORIZED);
        }

        post =
                post.update(
                        updatePostCommand.newTitle(),
                        updatePostCommand.newContent(),
                        updatePostCommand.newType());

        List<Long> newImageIdList = updatePostCommand.newImageIds();

        if (newImageIdList != null && !newImageIdList.isEmpty()) {
            List<Image> updatedImages = new ArrayList<>();

            for (long imageId : newImageIdList) {
                Image image =
                        imageRepository
                                .findById(imageId)
                                .orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));
                updatedImages.add(image.updatePostId(post.getId()));
            }

            imageRepository.saveAll(updatedImages);
        }

        postRepository.save(post);
    }

    @Override
    public void deletePost(final long postId, final long userId) {

        Post post =
                postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (!post.isOwnedBy(userId)) {
            throw new PostException(POST_NOT_AUTHORIZED);
        }

        List<Image> imageList = imageRepository.findByPostId(post.getId());

        imageRepository.deleteAll(imageList);
        postScrapRepository.deleteAllByPostId(post.getId());
        postLikeRepository.deleteAllByPostId(post.getId());
        commentLikeRepository.deleteAllByPostId(post.getId());

        commentRepository.deleteAllByPostId(post.getId());

        postRepository.delete(post);
    }
}
