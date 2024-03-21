package com.ys.notification.application.usecase;

import com.ys.notification.domain.CreateNotificationCommand;
import com.ys.notification.domain.Notification;

public interface ReserveNotificationUseCase {
    Notification reserve(CreateNotificationCommand command);
}
