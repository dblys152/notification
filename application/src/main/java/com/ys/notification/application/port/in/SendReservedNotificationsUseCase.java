package com.ys.notification.application.port.in;

import com.ys.notification.domain.entity.Notifications;

public interface SendReservedNotificationsUseCase {
    Notifications sendAll();
}
