package com.ys.notification.adapter.in;

import com.ys.notification.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationModel {
    Long id;
    NotificationType type;
    NotificationStatus status;
    LocalDateTime sentAt;
    SenderType senderType;
    String senderUserId;
    String destination;
    ReceiverType receiverType;
    String receiverId;
    String title;
    String contents;

    public static NotificationModel fromDomain(Notification notification) {
        return new NotificationModel(
                notification.getId().get(),
                notification.getType(),
                notification.getStatus(),
                notification.getSentAt(),
                notification.getSenderType(),
                notification.getSenderUserId(),
                notification.getDestination().getValue(),
                notification.getReceiverType(),
                notification.getReceiverId(),
                notification.getTitle(),
                notification.getContents()
        );
    }
}
