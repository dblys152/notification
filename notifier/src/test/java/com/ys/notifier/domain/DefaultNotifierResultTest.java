package com.ys.notifier.domain;

import com.ys.notification.domain.NotificationId;
import com.ys.notifier.domain.event.NotifierEvent;
import com.ys.notifier.domain.event.NotifierEventType;
import com.ys.notifier.fixture.SupportNotifierFixture;
import com.ys.shared.event.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class DefaultNotifierResultTest extends SupportNotifierFixture {
    private DefaultNotifierResult defaultNotifierResult;

    @BeforeEach
    void setUp() {
        defaultNotifierResult = DefaultNotifierResult.success(NOTIFICATION_ID);
    }

    @Test
    void 결과_이벤트를_발행한다() {
        DomainEventPublisher<NotifierEvent> domainEventPublisher = mock(DomainEventPublisher.class);

        defaultNotifierResult.publishEvent(domainEventPublisher);

        then(domainEventPublisher).should().publish(
                eq(NotifierEventType.EXECUTE_NOTIFIER_EVENT.name()),
                any(NotifierEvent.class),
                any(LocalDateTime.class));
    }
}