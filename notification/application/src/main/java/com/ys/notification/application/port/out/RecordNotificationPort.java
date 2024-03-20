package com.ys.notification.application.port.out;

import com.ys.notification.domain.Notification;
import com.ys.notification.domain.Notifications;

public interface RecordNotificationPort {
    Notification save(Notification notification);
    void saveAll(Notifications notifications);
}
