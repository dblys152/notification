package com.ys.notifier.domain;

import com.ys.notification.domain.Destination;
import com.ys.notification.domain.NotificationId;
import com.ys.notification.domain.NotificationType;
import com.ys.shared.utils.SelfValidating;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ExecuteNotifierCommand extends SelfValidating<ExecuteNotifierCommand> {
    @NotNull
    private NotificationId notificationId;

    @NotNull
    private NotificationType type;

    @Valid
    @NotNull
    private Destination destination;

    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank
    @Size(min = 1)
    private String contents;

    public ExecuteNotifierCommand(NotificationId notificationId, NotificationType type, Destination destination, String title, String contents) {
        this.notificationId = notificationId;
        this.type = type;
        this.destination = destination;
        this.title = title;
        this.contents = contents;
        validateSelf();
    }
}
