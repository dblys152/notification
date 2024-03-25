package com.ys.notifier.application.service;

import com.ys.notification.domain.event.NotificationBulkEvent;
import com.ys.notification.domain.event.NotificationEvent;
import com.ys.notifier.application.usecase.SendNotificationBulkUseCase;
import com.ys.notifier.domain.ExecuteNotifierCommand;
import com.ys.shared.utils.CommandFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExecuteNotifierProcessor implements Consumer<NotificationBulkEvent> {
    private final CommandFactory<NotificationEvent, ExecuteNotifierCommand> commandFactory;
    private final SendNotificationBulkUseCase sendNotificationBulkUseCase;

    @Override
    public void accept(NotificationBulkEvent notificationBulkEvent) {
        sendNotificationBulkUseCase.send(notificationBulkEvent.getItems().stream()
                .map(event -> commandFactory.create(event))
                .toList());
    }
}
