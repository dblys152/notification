package com.ys.notification.domain.command;

import com.ys.notification.domain.entity.NotificationType;

import java.util.HashMap;
import java.util.Map;

public class DefaultNotifierFinder implements NotifierFinder {
    private final Map<NotificationType, Notifier> candidatesMap = new HashMap<>();

    public DefaultNotifierFinder(
            EmailNotifier emailNotifier,
            CoolSmsNotifier coolSmsNotifier
    ) {
        candidatesMap.put(NotificationType.EMAIL, emailNotifier);
        candidatesMap.put(NotificationType.COOL_SMS, coolSmsNotifier);
    }

    @Override
    public Notifier getNotifier(NotificationType type) {
        return this.candidatesMap.get(type);
    }
}
