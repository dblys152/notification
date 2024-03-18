package com.ys.notification.domain.command;

import com.ys.notification.domain.entity.Notification;

public interface Notifier {
    void execute(Notification notification);
}
