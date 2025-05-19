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

@Table(name = "attendances")
@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Attendance extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @Column(name = "current_streak")
    private Long currentStreak; // 현재 연속 출석일

    @Column(name = "last_attended_date")
    private LocalDate lastAttendedDate; // 마지막 출석 날짜

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateCurrentStreak(long currentStreak) {
        this.currentStreak = currentStreak;
    }

    public void updateLastAttendedDate(LocalDate lastAttendedDate) {
        this.lastAttendedDate = lastAttendedDate;
    }
}
