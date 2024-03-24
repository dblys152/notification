package com.ys.notification.domain.event;

import com.ys.notification.domain.Notification;
import com.ys.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private Long notificationId;
    private NotificationType type;
    private String destination;
    private String title;
    private String contents;

    public static NotificationEvent fromDomain(Notification notification) {
        return new NotificationEvent(
                notification.getId().get(),
                notification.getType(),
                notification.getDestination().getValue(),
                notification.getTitle(),
                notification.getContents()
        );
    }
}
