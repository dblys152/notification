package com.ys.notification.domain;

import com.github.f4b6a3.tsid.TsidCreator;
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
    @Valid
    @NotNull
    private NotificationId id;

    @NotNull
    private NotificationType type;

    @NotNull
    private NotificationStatus status;

    @NotNull
    private LocalDateTime sentAt;

    @Valid
    @NotNull
    private Destination destination;

    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank
    @Size(min = 1)
    private String contents;

    @Valid
    @NotNull
    private Sender sender;

    @Valid
    @NotNull
    private Receiver receiver;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime modifiedAt;

    public static Notification of(
            NotificationId id,
            NotificationType type,
            NotificationStatus status,
            LocalDateTime sentAt,
            Destination destination,
            String title,
            String contents,
            Sender sender,
            Receiver receiver,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        return new Notification(
                id, type, status, sentAt, destination, title, contents, sender, receiver, createdAt, modifiedAt);
    }

    public static Notification create(CreateNotificationCommand command) {
        NotificationId notificationId = NotificationId.of(TsidCreator.getTsid256().toLong());
        LocalDateTime now = LocalDateTime.now();
        return new Notification(
                notificationId,
                command.getType(),
                NotificationStatus.RESERVED,
                command.getSentAt(),
                command.getDestination(),
                command.getTitle(),
                command.getContents(),
                command.getSender(),
                command.getReceiver(),
                now, now);
    }

    private void changeStatus(NotificationStatus status) {
        this.status = status;
        this.modifiedAt = LocalDateTime.now();
    }

    public void toWaiting() {
        if (!this.status.isReserved()) {
            throw new IllegalStateException("예약된 상태에서만 대기중 처리 할 수 있습니다.");
        }
        changeStatus(NotificationStatus.WAITING);
    }

    public void succeed() {
        if (!this.status.isWaiting()) {
            throw new IllegalStateException("대기중 상태에서만 성공 처리 할 수 있습니다.");
        }
        changeStatus(NotificationStatus.SUCCEEDED);
    }

    public void fail() {
        if (!this.status.isWaiting()) {
            throw new IllegalStateException("대기중 상태에서만 실패 처리 할 수 있습니다.");
        }
        changeStatus(NotificationStatus.FAILED);
    }
}
