package com.ripple.BE.learning.domain.learningset;

import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.learning.exception.errorcode.LearningErrorCode;
import com.ripple.BE.user.domain.type.Level;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LearningSetStat {

    private final Level level;
    private int quizCount;
    private int conceptCount;

    @Builder(access = AccessLevel.PRIVATE)
    public LearningSetStat(Level level, int quizCount, int conceptCount) {
        if (quizCount < 0 || conceptCount < 0) {
            throw new LearningException(LearningErrorCode.LEARNING_SET_STAT_ILLEGAL_ARGUMENT);
        }

        this.level = level;
        this.quizCount = quizCount;
        this.conceptCount = conceptCount;
    }

    public int getTotalCount() {
        return quizCount + conceptCount;
    }

    public static LearningSetStat withoutId(Level level, int quizCount, int conceptCount) {
        return LearningSetStat.builder()
                .level(level)
                .quizCount(quizCount)
                .conceptCount(conceptCount)
                .build();
    }

    public static LearningSetStat withId(Level level, int quizCount, int conceptCount) {
        return LearningSetStat.builder()
                .level(level)
                .quizCount(quizCount)
                .conceptCount(conceptCount)
                .build();
    }

    public void addConcepts(int count) {
        this.conceptCount += count;
    }

    public void addQuizzes(int count) {
        this.quizCount += count;
    }
}
