package com.ys.notification.adapter.out;

import com.ys.notification.adapter.out.persistence.NotificationEntity;
import com.ys.notification.adapter.out.persistence.NotificationRepository;
import com.ys.notification.application.port.out.LoadNotificationPort;
import com.ys.notification.application.port.out.RecordNotificationPort;
import com.ys.notification.domain.entity.Notification;
import com.ys.notification.domain.entity.NotificationStatus;
import com.ys.notification.domain.entity.Notifications;
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
    public void saveAll(Notifications notifications) {
        repository.saveAll(notifications.getItems().stream()
                .map(NotificationEntity::fromDomain)
                .toList());
    }

    @Override
    public Notifications findAllByStatusAndSentAtLessThanEqual(NotificationStatus status, LocalDateTime now) {
        List<NotificationEntity> entityList = repository.findAllByStatusAndSentAtLessThanEqual(status, now);
        return Notifications.of(entityList.stream()
                .map(e -> e.toDomain())
                .toList());
    }
}
