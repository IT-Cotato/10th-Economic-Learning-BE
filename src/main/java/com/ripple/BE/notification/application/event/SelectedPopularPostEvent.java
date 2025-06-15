package com.ripple.BE.notification.application.event;

import com.ripple.BE.post.domain.post.Post;

public record SelectedPopularPostEvent(Post post) {}
