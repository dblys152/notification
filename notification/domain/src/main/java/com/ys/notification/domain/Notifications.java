package com.ys.notification.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value(staticConstructor = "of")
public class Notifications {
    @NotNull
    List<Notification> items;

    public boolean isEmpty() {
        return this.items == null || this.items.isEmpty();
    }

    public Notifications empty() {
        return new Notifications(Collections.emptyList());
    }

    public void changeStatus(NotificationStatus status) {
        this.items.stream()
                .forEach(i -> i.changeStatus(status));
    }
}
