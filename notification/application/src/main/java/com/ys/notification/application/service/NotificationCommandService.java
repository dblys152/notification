package com.ys.notification.application.service;

import com.ys.notification.application.usecase.ChangeReservedToWaitingUseCase;
import com.ys.notification.application.usecase.ProcessSendingResultBulkUseCase;
import com.ys.notification.application.usecase.ProcessSendingResultUseCase;
import com.ys.notification.application.usecase.ReserveNotificationUseCase;
import com.ys.notification.domain.*;
import com.ys.notification.domain.event.NotificationBulkEvent;
import com.ys.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class NotificationCommandService implements ReserveNotificationUseCase, ChangeReservedToWaitingUseCase, ProcessSendingResultUseCase, ProcessSendingResultBulkUseCase {
    private final RecordNotificationPort recordNotificationPort;
    private final LoadNotificationPort loadNotificationPort;
    private final DomainEventPublisher<NotificationBulkEvent> domainEventPublisher;

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

        savedNotifications.eventPublish(domainEventPublisher);

        return savedNotifications;
    }

    @Override
    public Notification processSendingResult(ProcessSendingResultCommand command) {
        Notification notification = loadNotificationPort.findById(command.getNotificationId());

        switch (command.getStatus()) {
            case SUCCEEDED -> notification.succeed();
            case FAILED -> notification.fail();
        }
        Notification savedNotification = recordNotificationPort.save(notification);

        return savedNotification;
    }

    @Override
    public Notifications processSendingResults(List<ProcessSendingResultCommand> commandList) {
        Notifications notifications = loadNotificationPort.findAllById(commandList.stream()
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
