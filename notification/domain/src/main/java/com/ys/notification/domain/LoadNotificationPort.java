package com.ys.notification.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface LoadNotificationPort {
    Notifications findAllByStatusAndSentAtLessThanEqual(NotificationStatus status, LocalDateTime now);
    Notifications findAllByIds(List<NotificationId> ids);
}
