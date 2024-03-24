package com.ys.notification.domain;

import com.ys.notification.domain.event.NotificationBulkEvent;
import com.ys.notification.domain.event.NotificationEventType;
import com.ys.shared.event.DomainEventPublisher;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value(staticConstructor = "of")
public class Notifications {
    @NotNull
    List<Notification> items;

    public boolean isEmpty() {
        return this.items == null || this.items.isEmpty();
    }

    public void toWaiting() {
        this.items.stream()
                .forEach(i -> i.toWaiting());
    }

    public Notifications processSendingResults(List<ProcessSendingResultCommand> commandList) {
        Map<NotificationId, ProcessSendingResultCommand> commandMap = convertToMap(commandList);

        List<Notification> resultList = this.items.stream()
                .filter(notification -> commandMap.containsKey(notification.getId()) && notification.getStatus().isWaiting())
                .map(notification -> {
                    ProcessSendingResultCommand command = commandMap.get(notification.getId());
                    NotificationStatus commandStatus = command.getStatus();
                    switch (commandStatus) {
                        case SUCCEEDED -> notification.succeed();
                        case FAILED -> notification.fail();
                    }
                    return notification;
                })
                .toList();

        return Notifications.of(resultList);
    }

    private Map<NotificationId, ProcessSendingResultCommand> convertToMap(List<ProcessSendingResultCommand> commandList) {
        return commandList.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ProcessSendingResultCommand::getNotificationId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }
    
    public void eventPublish(DomainEventPublisher<NotificationBulkEvent> domainEventPublisher) {
        filterAndEventPublish(NotificationEventType.WAIT_EMAIL_NOTIFICATION_BULK_EVENT, NotificationType.EMAIL, domainEventPublisher);
        filterAndEventPublish( NotificationEventType.WAIT_SMS_NOTIFICATION_BULK_EVENT, NotificationType.COOL_SMS,domainEventPublisher);
    }

    private void filterAndEventPublish(NotificationEventType eventType, NotificationType type, DomainEventPublisher<NotificationBulkEvent> domainEventPublisher) {
        Notifications filteredNotifications = filterByType(type);
        if (!filteredNotifications.isEmpty()) {
            NotificationBulkEvent payload = NotificationBulkEvent.fromDomain(filteredNotifications);
            domainEventPublisher.publish(eventType.name(), payload, LocalDateTime.now());
        }
    }

    private Notifications filterByType(NotificationType type) {
        return new Notifications(this.items.stream()
                .filter(notification -> notification.getType().equals(type))
                .toList());
    }
}
