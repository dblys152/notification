package com.ys.notification.notifier;

import com.ys.notification.domain.NotificationType;

public interface NotifierFinder {
    Notifier getNotifier(NotificationType type);
}
