package com.ys.notification.application.port.out;

import com.ys.notification.domain.NotificationStatus;
import com.ys.notification.domain.Notifications;

import java.time.LocalDateTime;

public interface LoadNotificationPort {
    Notifications findAllByStatusAndSentAtLessThanEqual(NotificationStatus status, LocalDateTime now);
}
