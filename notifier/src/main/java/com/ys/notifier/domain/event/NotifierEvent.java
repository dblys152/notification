package com.ys.notifier.domain.event;

import com.ys.notification.domain.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifierEvent {
    Long notificationId;
    NotificationStatus status;
    LocalDateTime sentAt;
}
