package com.ys.notification.application.service;

import com.ys.notification.application.usecase.ChangeReservedToWaitingUseCase;
import com.ys.notification.application.usecase.ProcessSendingResultsUseCase;
import com.ys.notification.application.usecase.ReserveNotificationUseCase;
import com.ys.notification.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class NotificationCommandService implements ReserveNotificationUseCase, ChangeReservedToWaitingUseCase, ProcessSendingResultsUseCase {
    private RecordNotificationPort recordNotificationPort;
    private LoadNotificationPort loadNotificationPort;

    @Override
    public Notification reserve(CreateNotificationCommand command) {
        Notification notification = Notification.create(command);
        Notification savedNotification = recordNotificationPort.save(notification);

        return savedNotification;
    }

    @Override
    public Notifications changeReservedToWaiting() {
        Notifications reservedNotifications = loadNotificationPort.findAllByStatusAndSentAtLessThanEqual(
                NotificationStatus.RESERVED, LocalDateTime.now());

        if (reservedNotifications.isEmpty()) {
            return reservedNotifications;
        }

        reservedNotifications.toWaiting();
        Notifications savedNotifications = recordNotificationPort.saveAll(reservedNotifications);

        return savedNotifications;
    }

    @Override
    public Notifications processSendingResults(List<ProcessSendingResultCommand> commandList) {
        Notifications notifications = loadNotificationPort.findAllByIds(commandList.stream()
                .map(c -> c.getNotificationId())
                .toList());
        if (notifications.isEmpty()) {
            return notifications;
        }

        Notifications processedNotifications = notifications.processSendingResults(commandList);
        if (processedNotifications.isEmpty()) {
            return processedNotifications;
        }
        Notifications savedNotifications = recordNotificationPort.saveAll(processedNotifications);

        return savedNotifications;
    }
}
