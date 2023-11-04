package com.ys.notification.domain;

import com.ys.notification.infrastructure.utils.SelfValidating;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateNotificationCommand extends SelfValidating<CreateNotificationCommand> {
    @NotNull
    private NotificationType type;

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

    public CreateNotificationCommand(
            NotificationType type,
            LocalDateTime sentAt,
            SenderType senderType,
            String senderUserId,
            Destination destination,
            ReceiverType receiverType,
            String receiverId,
            String title,
            String contents
    ) {
        this.type = type;
        this.sentAt = sentAt;
        this.senderType = senderType;
        this.senderUserId = senderUserId;
        this.destination = destination;
        this.receiverType = receiverType;
        this.receiverId = receiverId;
        this.title = title;
        this.contents = contents;
        validateSelf();
    }
}
