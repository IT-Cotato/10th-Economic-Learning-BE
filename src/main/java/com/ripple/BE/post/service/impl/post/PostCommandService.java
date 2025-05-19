package com.ripple.BE.post.service.impl.post;

import static com.ripple.BE.image.exception.errorcode.ImageErrorCode.*;
import static com.ripple.BE.post.exception.errorcode.PostErrorCode.*;

import com.ripple.BE.image.domain.Image;
import com.ripple.BE.image.exception.ImageException;
import com.ripple.BE.image.repository.ImageRepository;
import com.ripple.BE.post.domain.post.Post;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.post.persistence.PostRepository;
import com.ripple.BE.post.persistence.jpa.entity.PostJpaEntity;
import com.ripple.BE.post.service.PostCommandUseCase;
import com.ripple.BE.post.service.command.CreatePostCommand;
import com.ripple.BE.post.service.command.UpdatePostCommand;
import com.ripple.BE.user.domain.User;
import com.ripple.BE.user.service.UserService;
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

    private final UserService userService;

    @Override
    public void createPost(final CreatePostCommand createPostCommand) {
        User user = userService.findUserById(createPostCommand.authorId());
        Post post =
                Post.of(
                        createPostCommand.title(),
                        createPostCommand.content(),
                        user,
                        createPostCommand.type(),
                        null);

        if (createPostCommand.imageIds() != null) {
            for (long imageId : createPostCommand.imageIds()) {
                Image image =
                        imageRepository
                                .findById(imageId)
                                .orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));
                image.setPost(PostJpaEntity.from(post)); // 추후 Post로 변경
                imageRepository.save(image);
            }
        }

        postRepository.save(post);
    }

    @Override
    public void updatePost(final UpdatePostCommand updatePostCommand) {
        Post post =
                postRepository
                        .findById(updatePostCommand.postId())
                        .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (post.isOwnedBy(updatePostCommand.authorId())) {
            throw new PostException(POST_NOT_AUTHORIZED);
        }
        post.update(
                updatePostCommand.newTitle(), updatePostCommand.newContent(), updatePostCommand.newType());
        List<Long> newImageIdList = updatePostCommand.newImageIds();

        if (newImageIdList != null && !newImageIdList.isEmpty()) {
            for (long imageId : newImageIdList) {
                Image image =
                        imageRepository
                                .findById(imageId)
                                .orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));
                image.setPost(PostJpaEntity.from(post)); // 추후 Post로 변경
                imageRepository.save(image);
            }
        }

        postRepository.update(post);
    }

    @Override
    public void deletePost(final long postId, final long userId) {

        Post post =
                postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (post.isOwnedBy(userId)) {
            throw new PostException(POST_NOT_AUTHORIZED);
        }

        List<Image> imageList = imageRepository.findByPostId(post.getId());

        imageRepository.deleteAll(imageList);
        postRepository.delete(post);
    }
}
