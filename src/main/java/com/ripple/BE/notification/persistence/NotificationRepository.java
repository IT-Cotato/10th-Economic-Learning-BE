package com.ripple.BE.notification.persistence;

import com.ripple.BE.notification.domain.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    void save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findByUserIdOrderByCreatedDateDesc(Long userId);

    long countByUserIdAndIsReadFalse(Long receiverId);

    void delete(Notification notification);

    void deleteAllByUserId(Long userId);
}
