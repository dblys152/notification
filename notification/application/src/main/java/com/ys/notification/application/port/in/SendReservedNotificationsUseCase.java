package com.ys.notification.application.port.in;

import com.ys.notification.domain.Notifications;

public interface SendReservedNotificationsUseCase {
    Notifications sendReservedNotifications();
}
