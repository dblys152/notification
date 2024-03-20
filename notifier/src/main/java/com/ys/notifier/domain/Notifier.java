package com.ys.notifier.domain;

import com.ys.notification.domain.entity.Notification;

public interface Notifier {
    boolean support(Notification notification);
    void execute(Notification notification);
}
