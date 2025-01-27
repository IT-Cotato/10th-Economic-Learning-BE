package com.ripple.BE.notification.repository;

import static com.ripple.BE.notification.domain.QNotification.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.notification.domain.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findByUserId(final long userId) {
        return queryFactory
                .selectFrom(notification)
                .where(notification.receiver.id.eq(userId))
                .orderBy(notification.createdDate.desc())
                .fetch();
    }

    @Override
    public long countByUserIdAndIsReadFalse(final long userId) {
        Long count =
                queryFactory
                        .select(notification.count())
                        .from(notification)
                        .where(notification.receiver.id.eq(userId).and(notification.isRead.isFalse()))
                        .fetchOne();

        return count == null ? 0 : count;
    }
}
