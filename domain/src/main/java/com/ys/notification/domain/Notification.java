package com.ys.notification.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Notification {
    @NotNull @Valid
    private NotificationId id;

    @NotNull
    private NotificationType type;

    @NotNull
    private NotificationStatus status;

    @NotNull
    private LocalDateTime sentAt;

    @NotNull
    private SenderType senderType;

    @Size(min = 1, max = 39)
    private String senderUserId;

    @NotNull @Valid
    private Destination destination;

    @NotNull
    private ReceiverType receiverType;

    @Size(max = 39)
    private String receiverId;

    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank
    @Size(min = 1)
    private String contents;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    public static Notification of(
            NotificationId id,
            NotificationType type,
            NotificationStatus status,
            LocalDateTime sentAt,
            SenderType senderType,
            String senderUserId,
            Destination destination,
            ReceiverType receiverType,
            String receiverId,
            String title,
            String contents,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            LocalDateTime deletedAt
    ) {
        return new Notification(
                id, type, status, sentAt, senderType, senderUserId, destination, receiverType, receiverId, title, contents, createdAt, modifiedAt, deletedAt);
    }

    public static Notification create(CreateNotificationCommand command) {
        command.getDestination().validate(command.getType());
        LocalDateTime now = LocalDateTime.now();
        return new Notification(
                NotificationId.of(null),
                command.getType(),
                NotificationStatus.RESERVED,
                command.getSentAt(),
                command.getSenderType(),
                command.getSenderUserId(),
                command.getDestination(),
                command.getReceiverType(),
                command.getReceiverId(),
                command.getTitle(),
                command.getContents(),
                now, now, null);
    }

    public void changeStatus(NotificationStatus status) {
        this.status = status;
        this.modifiedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
