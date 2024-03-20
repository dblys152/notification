package com.ys.notifier.domain;

import com.ys.notification.domain.entity.Notification;
import com.ys.notification.domain.entity.NotificationType;

public abstract class AbstractNotifier implements Notifier {
    private NotificationType type;
    private String activeProfile;

    public AbstractNotifier(NotificationType type, String activeProfile) {
        this.type = type;
        this.activeProfile = activeProfile;
    }

    @Override
    public boolean support(Notification notification) {
        return this.type.equals(notification.getType());
    }

    @Override
    public void execute(Notification notification) {
        _execute(notification);
    }

    protected abstract void _execute(Notification notification);

    protected boolean isTestProfile() {
        return this.activeProfile.equals("dev") || this.activeProfile.equals("test");
    }
}
