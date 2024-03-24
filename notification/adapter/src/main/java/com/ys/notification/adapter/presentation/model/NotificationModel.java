package com.ys.notification.adapter.presentation.model;

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
    String destination;
    String title;
    String contents;
    SenderType senderType;
    String senderUserId;
    ReceiverType receiverType;
    String receiverId;

    public static NotificationModel fromDomain(Notification notification) {
        Sender sender = notification.getSender();
        Receiver receiver = notification.getReceiver();
        return new NotificationModel(
                notification.getId().get(),
                notification.getType(),
                notification.getStatus(),
                notification.getSentAt(),
                notification.getDestination().getValue(),
                notification.getTitle(),
                notification.getContents(),
                sender.getSenderType(),
                sender.getSenderUserId(),
                receiver.getReceiverType(),
                receiver.getReceiverId()
        );
    }
}
