package com.ys.notification.application.service;

import com.ys.notification.application.port.in.ReserveNotificationUseCase;
import com.ys.notification.application.port.in.SendReservedNotificationsUseCase;
import com.ys.notification.application.port.out.LoadNotificationPort;
import com.ys.notification.application.port.out.RecordNotificationPort;
import com.ys.notification.domain.command.CreateNotificationCommand;
import com.ys.notification.domain.command.DefaultNotifierFinder;
import com.ys.notification.domain.command.Notifier;
import com.ys.notification.domain.entity.Notification;
import com.ys.notification.domain.entity.NotificationStatus;
import com.ys.notification.domain.entity.Notifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationCommandService implements ReserveNotificationUseCase, SendReservedNotificationsUseCase {
    private final RecordNotificationPort recordNotificationPort;
    private final LoadNotificationPort loadNotificationPort;
    private final DefaultNotifierFinder defaultNotifierFinder;

    @Override
    public Notification reserve(CreateNotificationCommand command) {
        Notification notification = Notification.create(command);

        Notification savedNotification = recordNotificationPort.save(notification);

        return savedNotification;
    }

    @Override
    public Notifications sendAll() {
        Notifications notifications = loadNotificationPort.findAllByStatusAndSentAtLessThanEqual(
                NotificationStatus.RESERVED, LocalDateTime.now());

        Notifications executedItems = Notifications.of(notifications.getItems().stream()
                .map(notification -> {
                    Notifier notifier = defaultNotifierFinder.getNotifier(notification.getType());
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
