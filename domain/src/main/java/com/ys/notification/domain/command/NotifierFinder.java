package com.ys.notification.domain.command;

import com.ys.notification.domain.entity.NotificationType;

public interface NotifierFinder {
    Notifier getNotifier(NotificationType type);
}
