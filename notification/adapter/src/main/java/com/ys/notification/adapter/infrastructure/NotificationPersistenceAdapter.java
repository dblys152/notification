package com.ys.notification.adapter.infrastructure;

import com.ys.notification.adapter.infrastructure.persistence.NotificationEntity;
import com.ys.notification.adapter.infrastructure.persistence.NotificationRepository;
import com.ys.notification.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements RecordNotificationPort, LoadNotificationPort {
    private final NotificationRepository repository;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = repository.save(NotificationEntity.fromDomain(notification));
        return entity.toDomain();
    }

    @Override
    public Notifications saveAll(Notifications notifications) {
        List<NotificationEntity> entityList = repository.saveAll(notifications.getItems().stream()
                .map(NotificationEntity::fromDomain)
                .toList());
        return Notifications.of(entityList.stream()
                .map(NotificationEntity::toDomain)
                .toList());
    }

    @Override
    public Notifications findAllByStatusAndSentAtLessThanEqual(NotificationStatus status, LocalDateTime now) {
        List<NotificationEntity> entityList = repository.findAllByStatusAndSentAtLessThanEqual(status, now);
        return Notifications.of(entityList.stream()
                .map(e -> e.toDomain())
                .toList());
    }

    @Override
    public Notifications findAllById(List<NotificationId> ids) {
        List<NotificationEntity> entityList = repository.findAllById(ids.stream()
                .map(notificationId -> notificationId.get())
                .toList());
        return Notifications.of(entityList.stream()
                .map(NotificationEntity::toDomain)
                .toList());
    }
}
