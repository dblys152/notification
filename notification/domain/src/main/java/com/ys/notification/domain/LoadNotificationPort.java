package com.ys.notification.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface LoadNotificationPort {
    Notification findById(NotificationId notificationId);
    Notifications findAllByStatusAndSentAtLessThanEqual(NotificationStatus status, LocalDateTime now);
    Notifications findAllById(List<NotificationId> ids);
}
