package com.ys.notification.application.service;

import com.ys.notification.application.usecase.ProcessSendingResultUseCase;
import com.ys.notification.domain.NotificationId;
import com.ys.notification.domain.ProcessSendingResultCommand;
import com.ys.notification.domain.notifier.NotifierEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ProcessSendingResultProcessor implements Consumer<NotifierEvent> {
    private final ProcessSendingResultUseCase processSendingResultUseCase;

    @Override
    public void accept(NotifierEvent notifierEvent) {
        processSendingResultUseCase.processSendingResult(new ProcessSendingResultCommand(
                NotificationId.of(notifierEvent.getNotificationId()), notifierEvent.getStatus()));
    }
}
