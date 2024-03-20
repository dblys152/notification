package com.ys.notification.application.service;

import com.ys.notification.application.port.in.SendReservedNotificationsUseCase;
import com.ys.notification.application.port.out.LoadNotificationPort;
import com.ys.notification.application.port.out.RecordNotificationPort;
import com.ys.notification.domain.command.notifier.DefaultNotifierFinder;
import com.ys.notification.domain.command.notifier.Notifier;
import com.ys.notification.domain.NotificationStatus;
import com.ys.notification.domain.Notifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class SendReservedNotificationsService implements SendReservedNotificationsUseCase {
    private final RecordNotificationPort recordNotificationPort;
    private final LoadNotificationPort loadNotificationPort;
    private final DefaultNotifierFinder defaultNotifierFinder;

    @Override
    public Notifications sendReservedNotifications() {
        Notifications notifications = loadNotificationPort.findAllByStatusAndSentAtLessThanEqual(
                NotificationStatus.RESERVED, LocalDateTime.now());

        if ()

        if (!notifications.isEmpty()) {
            notifications.changeStatus(NotificationStatus.WAITING);
            recordNotificationPort.saveAll(notifications);
        }

        Notifications executedItems = Notifications.of(notifications.getItems().stream()
                .map(notification -> {
                    Notifier notifier = defaultNotifierFinder.getNotifier(notification);
                    try {
                        notifier.execute(notification);
                        notification.changeStatus(NotificationStatus.SENT);
                        return notification;
                    } catch (IllegalStateException ex) {
                        log.error(ex.getMessage());
                        notification.changeStatus(NotificationStatus.FAILED);
                        return notification;
                    }
                })
                .toList());
        if (!executedItems.isEmpty()) {
            recordNotificationPort.saveAll(executedItems);
        }

        return executedItems;
    }
}
