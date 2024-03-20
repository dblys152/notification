package com.ys.notifier.domain;

import com.ys.notification.domain.entity.Notification;

import java.util.List;

public class DefaultNotifierFinder implements NotifierFinder {
    private final List<Notifier> candidates;

    public DefaultNotifierFinder(List<Notifier> candidates) {
        this.candidates = candidates;
    }

    @Override
    public Notifier getNotifier(Notification notification) {
        return candidates.stream()
                .filter(p -> p.support(notification))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
