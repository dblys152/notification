package com.ys.notification.adapter.out.persistence;

import com.ys.notification.domain.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION_LIST")
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID", nullable = false)
    private Long id;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(name = "SENT_AT", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "SENDER_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @Column(name = "SENDER_USER_ID")
    @Size(min = 1, max = 39)
    private String senderUserId;

    @Column(name = "DESTINATION", nullable = false)
    @Size(min = 1, max = 30)
    private String destination;

    @Column(name = "RECEIVER_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReceiverType receiverType;

    @Column(name = "RECEIVER_ID")
    @Size(max = 39)
    private String receiverId;

    @Column(name = "TITLE", nullable = false)
    @Size(min = 1, max = 200)
    private String title;

    @Column(name = "CONTENTS", nullable = false)
    @Size(min = 1)
    private String contents;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;

    public static NotificationEntity fromDomain(Notification notification) {
        return new NotificationEntity(
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
                notification.getContents(),
                notification.getCreatedAt(),
                notification.getModifiedAt(),
                notification.getDeletedAt()
        );
    }

    public Notification toDomain() {
        return Notification.of(
                NotificationId.of(this.id),
                this.type,
                this.status,
                this.sentAt,
                this.senderType,
                this.senderUserId,
                Destination.of(this.destination),
                this.receiverType,
                this.receiverId,
                this.title,
                this.contents,
                this.createdAt,
                this.modifiedAt,
                this.deletedAt
        );
    }
}
