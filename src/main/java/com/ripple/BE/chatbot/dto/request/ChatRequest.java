package com.ripple.BE.chatbot.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank @Max(1000) String message) {}
