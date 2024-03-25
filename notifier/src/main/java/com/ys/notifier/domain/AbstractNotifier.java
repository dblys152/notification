package com.ys.notifier.domain;

import com.ys.notification.domain.NotificationType;

public abstract class AbstractNotifier implements Notifier<ExecuteNotifierCommand, NotifierResult> {
    private NotificationType type;
    private String activeProfile;

    public AbstractNotifier(NotificationType type, String activeProfile) {
        this.type = type;
        this.activeProfile = activeProfile;
    }

    @Override
    public boolean support(ExecuteNotifierCommand factor) {
        return this.type.equals(factor.getType());
    }

    @Override
    public NotifierResult execute(ExecuteNotifierCommand factor) {
        return _execute(factor);
    }

    protected abstract NotifierResult _execute(ExecuteNotifierCommand factor);

    protected boolean isTestProfile() {
        return this.activeProfile.equals("dev") || this.activeProfile.equals("test");
    }
}
