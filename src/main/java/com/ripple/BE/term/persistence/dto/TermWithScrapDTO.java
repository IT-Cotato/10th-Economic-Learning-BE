package com.ripple.BE.term.persistence.dto;

public record TermWithScrapDTO(
        Long id, String title, String description, String initial, boolean isScrapped) {}
