package com.ys.notification.domain.event;

import com.ys.notification.domain.Notifications;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationBulkEvent {
    private List<NotificationEvent> items;

    public static NotificationBulkEvent fromDomain(Notifications notifications) {
        return new NotificationBulkEvent(notifications.getItems().stream()
                .map(NotificationEvent::fromDomain)
                .toList());
    }
}
