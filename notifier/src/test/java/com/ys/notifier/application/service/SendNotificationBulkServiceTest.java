package com.ys.notifier.application.service;

import com.ys.notifier.domain.DefaultNotifierFinder;
import com.ys.notifier.domain.ExecuteNotifierCommand;
import com.ys.notifier.domain.Notifier;
import com.ys.notifier.domain.NotifierResult;
import com.ys.notifier.domain.event.NotifierEvent;
import com.ys.shared.event.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendNotificationBulkServiceTest {
    @InjectMocks
    private SendNotificationBulkService sut;

    @Mock
    private DefaultNotifierFinder defaultNotifierFinder;
    @Mock
    private DomainEventPublisher<NotifierEvent> domainEventPublisher;

    @Test
    void 알림을_일괄_발송한다() {
        ExecuteNotifierCommand command1 = mock(ExecuteNotifierCommand.class);
        ExecuteNotifierCommand command2 = mock(ExecuteNotifierCommand.class);
        List<ExecuteNotifierCommand> commandList = List.of(command1, command2);

        Notifier<ExecuteNotifierCommand, NotifierResult> notifier1 = mock(Notifier.class);
        Notifier<ExecuteNotifierCommand, NotifierResult> notifier2 = mock(Notifier.class);
        given(defaultNotifierFinder.getNotifier(command1)).willReturn(notifier1);
        given(defaultNotifierFinder.getNotifier(command2)).willReturn(notifier2);

        NotifierResult result1 = mock(NotifierResult.class);
        NotifierResult result2 = mock(NotifierResult.class);
        given(notifier1.execute(command1)).willReturn(result1);
        given(notifier2.execute(command2)).willReturn(result2);

        doNothing().when(result1).publishEvent(domainEventPublisher);
        doNothing().when(result2).publishEvent(domainEventPublisher);

        sut.send(commandList);

        assertAll(
                () -> then(defaultNotifierFinder).should().getNotifier(command1),
                () -> then(defaultNotifierFinder).should().getNotifier(command2),
                () -> then(notifier1).should().execute(command1),
                () -> then(notifier2).should().execute(command2),
                () -> then(result1).should().publishEvent(domainEventPublisher),
                () -> then(result2).should().publishEvent(domainEventPublisher)
        );
    }
}