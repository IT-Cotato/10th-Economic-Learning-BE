package com.ripple.BE.user.domain;

import com.ripple.BE.global.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "quests")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quest extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int quizCompletedCount;

    private int conceptCompletedCount;

    private int articleCompletedCount;

    private LocalDate lastUpdatedDate; // 퀘스트 완료 날짜

    public void resetQuests() {
        this.quizCompletedCount = 0;
        this.conceptCompletedCount = 0;
        this.articleCompletedCount = 0;
        this.lastUpdatedDate = LocalDate.now();
    }

    public void updateQuizCompletedCount() {
        this.quizCompletedCount++;
    }

    public void updateConceptCompletedCount() {
        this.conceptCompletedCount++;
    }

    public void updateArticleCompletedCount() {
        this.articleCompletedCount++;
    }
}
