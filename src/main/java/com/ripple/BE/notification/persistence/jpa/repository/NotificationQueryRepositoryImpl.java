package com.ripple.BE.notification.persistence.jpa.repository;

import static com.ripple.BE.notification.persistence.jpa.entity.QNotificationJpaEntity.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ripple.BE.notification.persistence.jpa.entity.NotificationJpaEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NotificationJpaEntity> findByUserId(final long userId) {
        return queryFactory
                .selectFrom(notificationJpaEntity)
                .where(notificationJpaEntity.receiverId.eq(userId))
                .orderBy(notificationJpaEntity.createdDate.desc())
                .fetch();
    }

    @Override
    public long countByUserIdAndIsReadFalse(final long userId) {
        Long count =
                queryFactory
                        .select(notificationJpaEntity.count())
                        .from(notificationJpaEntity)
                        .where(
                                notificationJpaEntity
                                        .receiverId
                                        .eq(userId)
                                        .and(notificationJpaEntity.isRead.isFalse()))
                        .fetchOne();

        return count == null ? 0 : count;
    }
}
