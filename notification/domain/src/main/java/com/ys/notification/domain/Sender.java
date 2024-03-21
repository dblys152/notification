package com.ys.notification.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value(staticConstructor = "of")
public class Sender {
    @NotNull
    private SenderType senderType;

    @Size(min = 1, max = 39)
    private String senderUserId;
}
