package com.ys.notification.adapter.infrastructure.persistence;

import com.ys.notification.domain.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATIONS")
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class NotificationEntity {
    @Id
    @Column(name = "NOTIFICATION_ID", nullable = false)
    private Long notificationId;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(name = "SENT_AT", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "DESTINATION", nullable = false)
    @Size(min = 1, max = 30)
    private String destination;

    @Column(name = "TITLE", nullable = false)
    @Size(min = 1, max = 200)
    private String title;

    @Column(name = "CONTENTS", nullable = false)
    @Size(min = 1)
    private String contents;

    @Column(name = "SENDER_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @Column(name = "SENDER_USER_ID")
    @Size(min = 1, max = 39)
    private String senderUserId;

    @Column(name = "RECEIVER_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReceiverType receiverType;

    @Column(name = "RECEIVER_ID")
    @Size(max = 39)
    private String receiverId;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT", nullable = false)
    private LocalDateTime modifiedAt;

    public static NotificationEntity fromDomain(Notification notification) {
        Sender sender = notification.getSender();
        Receiver receiver = notification.getReceiver();
        return new NotificationEntity(
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
                receiver.getReceiverId(),
                notification.getCreatedAt(),
                notification.getModifiedAt()
        );
    }

    public Notification toDomain() {
        return Notification.of(
                NotificationId.of(this.notificationId),
                this.type,
                this.status,
                this.sentAt,
                Destination.of(this.type, this.destination),
                this.title,
                this.contents,
                Sender.of(this.senderType, this.senderUserId),
                Receiver.of(this.receiverType, this.receiverId),
                this.createdAt,
                this.modifiedAt
        );
    }
}
