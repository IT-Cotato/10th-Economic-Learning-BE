package com.ripple.BE.post.domain;

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

@Table(name = "today_toktok")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TodayToktok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post; // 오늘의 경제 톡톡 주제 선정 게시글

    @Column(name = "selected_date", nullable = false)
    private LocalDate selectedDate; // 선정된 날짜

    public static TodayToktok toTodayToktok(Post post, LocalDate selectedDate) {
        return TodayToktok.builder().post(post).selectedDate(selectedDate).build();
    }
}
