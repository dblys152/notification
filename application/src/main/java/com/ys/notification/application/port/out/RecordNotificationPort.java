package com.ys.notification.application.port.out;

import com.ys.notification.domain.Notification;

public interface RecordNotificationPort {
    Notification save(Notification notification);
}
