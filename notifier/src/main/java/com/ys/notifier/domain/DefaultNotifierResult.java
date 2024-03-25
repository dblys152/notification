package com.ys.notifier.domain;

import com.ys.notification.domain.NotificationId;
import com.ys.notification.domain.NotificationStatus;
import com.ys.notifier.domain.event.NotifierEvent;
import com.ys.notifier.domain.event.NotifierEventType;
import com.ys.shared.event.DomainEventPublisher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DefaultNotifierResult implements NotifierResult {
    private NotificationId notificationId;
    private NotificationStatus status;

    public static DefaultNotifierResult success(NotificationId notificationId) {
        return new DefaultNotifierResult(notificationId, NotificationStatus.SUCCEEDED);
    }

    public static DefaultNotifierResult fail(NotificationId notificationId) {
        return new DefaultNotifierResult(notificationId, NotificationStatus.FAILED);
    }

    @Override
    public void publishEvent(DomainEventPublisher<NotifierEvent> domainEventPublisher) {
        LocalDateTime now = LocalDateTime.now();
        domainEventPublisher.publish(
                NotifierEventType.EXECUTE_NOTIFIER_EVENT.name(),
                new NotifierEvent(this.notificationId.get(), status, now),
                now
        );
    }
}
