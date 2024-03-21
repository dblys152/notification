package com.ys.notification.application.service;

import com.ys.notification.application.usecase.model.ReserveNotificationRequest;
import com.ys.notification.domain.CreateNotificationCommand;
import com.ys.notification.domain.Destination;
import com.ys.notification.domain.Receiver;
import com.ys.notification.domain.Sender;
import com.ys.shared.exception.BadRequestException;
import com.ys.shared.utils.CommandFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateNotificationCommandFactory implements CommandFactory<ReserveNotificationRequest, CreateNotificationCommand> {
    @Override
    public CreateNotificationCommand create(ReserveNotificationRequest request) {
        try {
            return new CreateNotificationCommand(
                    request.getType(),
                    request.getSentAt(),
                    Destination.of(request.getType(), request.getDestination()),
                    request.getTitle(),
                    request.getContents(),
                    Sender.of(request.getSenderType(), request.getSenderUserId()),
                    Receiver.of(request.getReceiverType(), request.getReceiverId())
            );
        }  catch (IllegalArgumentException | IllegalStateException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
}
