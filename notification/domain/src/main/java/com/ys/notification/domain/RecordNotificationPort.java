package com.ys.notification.domain;

public interface RecordNotificationPort {
    Notification save(Notification notification);
    Notifications saveAll(Notifications notifications);
}
