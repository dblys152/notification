package com.ys.notification.domain;

public enum NotificationStatus {
    RESERVED,
    WAITING,
    SUCCEEDED,
    FAILED
    ;

    public boolean isReserved() {
        return this.equals(RESERVED);
    }

    public boolean isWaiting() {
        return this.equals(WAITING);
    }

    public boolean isSucceeded() {
        return this.equals(SUCCEEDED);
    }

    public boolean isFailed() {
        return this.equals(FAILED);
    }
}
