package com.ys.notification.application.service;

import com.ys.notification.application.port.in.ReserveNotificationRequest;
import com.ys.notification.domain.CreateNotificationCommand;
import com.ys.notification.domain.Destination;
import com.ys.shared.exception.BadRequestException;
import com.ys.shared.utils.CommandFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateNotificationCommandFactory implements CommandFactory<ReserveNotificationRequest, CreateNotificationCommand> {
    @Override
    public CreateNotificationCommand create(ReserveNotificationRequest request) {
        try {
            Destination destination = Destination.of(request.getDestination());
            destination.validate(request.getType());
            return new CreateNotificationCommand(
                    request.getType(),
                    request.getSentAt(),
                    request.getSenderType(),
                    request.getSenderUserId(),
                    destination,
                    request.getReceiverType(),
                    request.getReceiverId(),
                    request.getTitle(),
                    request.getContents()
            );
        }  catch (IllegalArgumentException | IllegalStateException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
}
