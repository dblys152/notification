package com.ys.notification.application.service;

import com.ys.notification.application.port.in.ReserveNotificationUseCase;
import com.ys.notification.application.port.out.RecordNotificationPort;
import com.ys.notification.domain.CreateNotificationCommand;
import com.ys.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReserveNotificationService implements ReserveNotificationUseCase {
    private RecordNotificationPort recordNotificationPort;

    @Override
    public Notification reserve(CreateNotificationCommand command) {
        Notification notification = Notification.create(command);

        Notification savedNotification = recordNotificationPort.save(notification);

        return savedNotification;
    }
}
