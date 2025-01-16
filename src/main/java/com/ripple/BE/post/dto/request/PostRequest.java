package com.ripple.BE.post.dto.request;

import com.ripple.BE.post.domain.type.PostType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotNull @Size(min = 2, max = 50) String title,
        @Size(min = 2, max = 3500) String content,
        PostType type) {}
