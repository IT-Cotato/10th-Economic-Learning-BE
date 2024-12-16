package com.ripple.BE.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String accountEmail, @NotNull String password) {}
