package com.ys.notification.adapter.out;

import com.ys.notification.adapter.out.persistence.NotificationEntity;
import com.ys.notification.adapter.out.persistence.NotificationRepository;
import com.ys.notification.application.port.out.RecordNotificationPort;
import com.ys.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements RecordNotificationPort {
    private final NotificationRepository repository;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = repository.save(NotificationEntity.fromDomain(notification));
        return entity.toDomain();
    }
}
