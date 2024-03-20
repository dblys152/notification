package com.ys.notification.application.port.in;

import com.ys.notification.domain.NotificationType;
import com.ys.notification.domain.ReceiverType;
import com.ys.notification.domain.SenderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReserveNotificationRequest {
    @NotNull
    private NotificationType type;

    @NotNull
    private LocalDateTime sentAt;

    @NotNull
    private SenderType senderType;

    @Size(min = 1, max = 39)
    private String senderUserId;

    @NotBlank
    @Size(min = 1, max = 30)
    private String destination;

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
}
