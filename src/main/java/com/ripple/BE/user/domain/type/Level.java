package com.ripple.BE.user.domain.type;

import java.util.Optional;

public enum Level {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED;

    private static final int BEGINNER_SCORE = 6; // 초급 레벨의 점수 기준
    private static final int INTERMEDIATE_SCORE = 12; // 중급 레벨의 점수 기준

    // 다음 레벨을 반환하는 메서드
    public Optional<Level> nextLevel() {
        return switch (this) {
            case BEGINNER -> Optional.of(INTERMEDIATE);
            case INTERMEDIATE -> Optional.of(ADVANCED);
            case ADVANCED -> Optional.empty();
        };
    }

    // 레벨 테스트 점수에 따라 레벨을 반환하는 메서드
    public static Level fromScore(final int score) {
        if (score <= BEGINNER_SCORE) return BEGINNER;
        if (score <= INTERMEDIATE_SCORE) return INTERMEDIATE;
        return ADVANCED;
    }
}
