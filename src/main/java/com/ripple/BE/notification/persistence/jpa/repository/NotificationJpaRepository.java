package com.ripple.BE.notification.persistence.jpa.repository;

import com.ripple.BE.notification.persistence.jpa.entity.NotificationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository
        extends JpaRepository<NotificationJpaEntity, Long>, NotificationQueryRepository {
    void deleteAllByReceiverId(Long userId);
}
