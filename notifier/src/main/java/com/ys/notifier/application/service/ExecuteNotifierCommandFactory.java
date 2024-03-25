package com.ys.notifier.application.service;

import com.ys.notification.domain.Destination;
import com.ys.notification.domain.NotificationId;
import com.ys.notification.domain.event.NotificationEvent;
import com.ys.notifier.domain.ExecuteNotifierCommand;
import com.ys.shared.exception.BadRequestException;
import com.ys.shared.utils.CommandFactory;
import org.springframework.stereotype.Component;

@Component
public class ExecuteNotifierCommandFactory implements CommandFactory<NotificationEvent, ExecuteNotifierCommand> {
    @Override
    public ExecuteNotifierCommand create(NotificationEvent request) {
        try {
            return new ExecuteNotifierCommand(
                    NotificationId.of(request.getNotificationId()),
                    request.getType(),
                    Destination.of(request.getType(), request.getDestination()),
                    request.getTitle(),
                    request.getContents()
            );
        } catch (IllegalArgumentException | IllegalStateException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
}
