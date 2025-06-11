package com.ripple.BE.learning.domain.quiz;

import com.ripple.BE.learning.domain.type.Type;
import com.ripple.BE.user.domain.type.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Quiz {

    private final Long id;
    private final Level level;
    private final Type type;
    private final String name;
    private final String question;
    private final String answer;
    private final String explanation;
    private final Long learningSetId;
    private final String learningSetName;
    private final List<Choice> choices;

    @Builder(access = AccessLevel.PRIVATE)
    private Quiz(
            Long id,
            Level level,
            Type type,
            String name,
            String question,
            String answer,
            String explanation,
            Long learningSetId,
            String learningSetName,
            List<Choice> choices) {
        this.id = id;
        this.level = level;
        this.type = type;
        this.name = name;
        this.question = question;
        this.answer = answer;
        this.explanation = explanation;
        this.learningSetId = learningSetId;
        this.learningSetName = learningSetName;
        this.choices = choices;
    }

    public static Quiz withId(
            Long id,
            Level level,
            Type type,
            String name,
            String question,
            String answer,
            String explanation,
            Long learningSetId,
            String learningSetName,
            List<Choice> choices) {
        return Quiz.builder()
                .id(id)
                .level(level)
                .type(type)
                .name(name)
                .question(question)
                .answer(answer)
                .explanation(explanation)
                .learningSetId(learningSetId)
                .learningSetName(learningSetName)
                .choices(choices)
                .build();
    }

    public static Quiz withoutId(
            Level level,
            Type type,
            String name,
            String question,
            String answer,
            String explanation,
            Long learningSetId,
            String learningSetName,
            List<Choice> choices) {
        return Quiz.builder()
                .level(level)
                .type(type)
                .name(name)
                .question(question)
                .answer(answer)
                .explanation(explanation)
                .learningSetId(learningSetId)
                .learningSetName(learningSetName)
                .choices(choices)
                .build();
    }

    public Quiz shuffleChoices() {
        if (this.type == Type.OX) {
            return this; // OX는 셔플 안 함
        }

        List<Choice> copied = new ArrayList<>(this.choices);
        Collections.shuffle(copied);

        return Quiz.withId(
                this.id,
                this.level,
                this.type,
                this.name,
                this.question,
                this.answer,
                this.explanation,
                this.learningSetId,
                this.learningSetName,
                copied);
    }

    public boolean isCorrectAnswer(String choiceContent) {
        return this.answer.trim().equals(choiceContent.trim());
    }
}
