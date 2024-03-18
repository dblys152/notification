package com.ys.notification.application.port.out;

import com.ys.notification.domain.entity.Notification;
import com.ys.notification.domain.entity.Notifications;

public interface RecordNotificationPort {
    Notification save(Notification notification);
    void saveAll(Notifications notifications);
}
