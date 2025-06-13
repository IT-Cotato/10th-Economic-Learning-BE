package com.ripple.BE.notification.persistence.jpa.repository;

import com.ripple.BE.notification.persistence.jpa.entity.NotificationJpaEntity;
import java.util.List;

public interface NotificationQueryRepository {

    List<NotificationJpaEntity> findByUserId(final long userId);

    long countByUserIdAndIsReadFalse(final long userId);
}
