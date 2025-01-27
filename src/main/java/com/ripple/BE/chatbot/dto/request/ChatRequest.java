package com.ripple.BE.chatbot.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank String message) {}
