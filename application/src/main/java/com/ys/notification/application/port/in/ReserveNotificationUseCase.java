package com.ys.notification.application.port.in;

import com.ys.notification.domain.command.CreateNotificationCommand;
import com.ys.notification.domain.entity.Notification;

public interface ReserveNotificationUseCase {
    Notification reserve(CreateNotificationCommand command);
}
