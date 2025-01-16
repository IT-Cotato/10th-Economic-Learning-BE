package com.ripple.BE.post.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(@NotNull @Size(min = 1, max = 255) String content) {}
