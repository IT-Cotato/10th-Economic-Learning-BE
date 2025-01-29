package com.ripple.BE.notification.repository;

import com.ripple.BE.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository
        extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {}
