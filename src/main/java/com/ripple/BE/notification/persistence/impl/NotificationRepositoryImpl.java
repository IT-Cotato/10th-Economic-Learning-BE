package com.ripple.BE.notification.persistence.impl;

import com.ripple.BE.notification.domain.Notification;
import com.ripple.BE.notification.persistence.NotificationRepository;
import com.ripple.BE.notification.persistence.jpa.entity.NotificationJpaEntity;
import com.ripple.BE.notification.persistence.jpa.repository.NotificationJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public void save(Notification notification) {
        notificationJpaRepository.save(NotificationJpaEntity.from(notification));
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id).map(NotificationJpaEntity::toModel);
    }

    @Override
    public List<Notification> findByUserIdOrderByCreatedDateDesc(Long userId) {
        return notificationJpaRepository.findByUserId(userId).stream()
                .map(NotificationJpaEntity::toModel)
                .toList();
    }

    @Override
    public long countByUserIdAndIsReadFalse(Long receiverId) {
        return notificationJpaRepository.countByUserIdAndIsReadFalse(receiverId);
    }

    @Override
    public void delete(Notification notification) {
        notificationJpaRepository.delete(NotificationJpaEntity.from(notification));
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        notificationJpaRepository.deleteAllByUserId(userId);
    }
}
