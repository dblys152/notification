package com.ys.notifier.domain;

import com.ys.notification.domain.entity.Notification;

public interface NotifierFinder {
    Notifier getNotifier(Notification notification);
}
