package com.ys.notifier.application.service;

import com.ys.notifier.application.usecase.SendNotificationBulkUseCase;
import com.ys.notifier.domain.DefaultNotifierFinder;
import com.ys.notifier.domain.ExecuteNotifierCommand;
import com.ys.notifier.domain.Notifier;
import com.ys.notifier.domain.NotifierResult;
import com.ys.notifier.domain.event.NotifierEvent;
import com.ys.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class SendNotificationBulkService implements SendNotificationBulkUseCase {
    private final DefaultNotifierFinder defaultNotifierFinder;
    private final DomainEventPublisher<NotifierEvent> domainEventPublisher;

    @Override
    public void send(List<ExecuteNotifierCommand> commandList) {
        commandList.forEach(command -> {
            Notifier<ExecuteNotifierCommand, NotifierResult> notifier = defaultNotifierFinder.getNotifier(command);

            NotifierResult result = notifier.execute(command);

            result.publishEvent(domainEventPublisher);
        });
    }
}
