package com.ripple.BE.notification.repository;

import com.ripple.BE.notification.domain.Notification;
import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findByUserId(final long userId);

    long countByUserIdAndIsReadFalse(final long userId);
}
