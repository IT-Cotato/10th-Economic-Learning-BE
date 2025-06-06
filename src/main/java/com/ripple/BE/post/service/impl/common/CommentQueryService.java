package com.ripple.BE.post.service.impl.common;

import com.ripple.BE.post.domain.comment.CommentLike;
import com.ripple.BE.post.dto.response.CommentResponseDTO;
import com.ripple.BE.post.persistence.CommentLikeRepository;
import com.ripple.BE.post.persistence.CommentRepository;
import com.ripple.BE.post.persistence.dto.CommentWithUserDTO;
import com.ripple.BE.post.service.CommentQueryUseCase;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService implements CommentQueryUseCase {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public List<CommentResponseDTO> getComments(long postId, long userId) {
        // 1. 댓글 + 작성자 정보 한번에 조회
        List<CommentWithUserDTO> allComments = commentRepository.findAllByPostIdWithUser(postId);
        if (allComments.isEmpty()) {
            return List.of();
        }

        // 2. 해당 사용자가 좋아요 누른 댓글 ID 목록 조회
        Set<Long> likedCommentIds = fetchLikedCommentIds(userId, allComments);

        // 3. 자식 댓글을 parentId 기준으로 그룹핑
        Map<Long, List<CommentWithUserDTO>> childrenGroupedByParent =
                allComments.stream()
                        .filter(comment -> comment.parentId() != null)
                        .collect(Collectors.groupingBy(CommentWithUserDTO::parentId));

        // 4. 루트 댓글만 변환 + 자식 댓글 포함한 응답 DTO로 구성
        return allComments.stream()
                .filter(comment -> comment.parentId() == null)
                .map(root -> mapToResponseDTO(root, userId, likedCommentIds, childrenGroupedByParent))
                .toList();
    }

    // 사용자 기준 좋아요한 댓글 ID 집합 조회
    private Set<Long> fetchLikedCommentIds(long userId, List<CommentWithUserDTO> comments) {
        Set<Long> commentIds =
                comments.stream().map(CommentWithUserDTO::id).collect(Collectors.toSet());

        return commentLikeRepository.findByUserIdAndCommentIdIn(userId, commentIds).stream()
                .map(CommentLike::getCommentId)
                .collect(Collectors.toSet());
    }

    // 루트 댓글 및 자식 댓글을 포함한 응답 DTO로 매핑
    private CommentResponseDTO mapToResponseDTO(
            CommentWithUserDTO root,
            long userId,
            Set<Long> likedIds,
            Map<Long, List<CommentWithUserDTO>> childrenMap) {
        List<CommentResponseDTO> children =
                childrenMap.getOrDefault(root.id(), List.of()).stream()
                        .map(
                                child ->
                                        CommentResponseDTO.of(
                                                child, isAuthor(child, userId), isLiked(child, likedIds), List.of()))
                        .toList();

        return CommentResponseDTO.of(root, isAuthor(root, userId), isLiked(root, likedIds), children);
    }

    private boolean isAuthor(CommentWithUserDTO comment, long userId) {
        return comment.commenterId().equals(userId);
    }

    private boolean isLiked(CommentWithUserDTO comment, Set<Long> likedIds) {
        return likedIds.contains(comment.id());
    }
}
