package com.ys.notification.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value(staticConstructor = "of")
public class Receiver {
    @NotNull
    private ReceiverType receiverType;

    @Size(max = 39)
    private String receiverId;
}
