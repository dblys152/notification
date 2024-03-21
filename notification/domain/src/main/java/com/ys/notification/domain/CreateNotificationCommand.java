package com.ys.notification.domain;

import com.ys.shared.utils.SelfValidating;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateNotificationCommand extends SelfValidating<CreateNotificationCommand> {
    @NotNull
    private NotificationType type;

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

    public CreateNotificationCommand(
            NotificationType type,
            LocalDateTime sentAt,
            Destination destination,
            String title,
            String contents,
            Sender sender,
            Receiver receiver
    ) {
        this.type = type;
        this.sentAt = sentAt;
        this.destination = destination;
        this.title = title;
        this.contents = contents;
        this.sender = sender;
        this.receiver = receiver;
        validateSelf();
    }
}
