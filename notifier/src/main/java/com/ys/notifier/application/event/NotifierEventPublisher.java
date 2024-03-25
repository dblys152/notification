package com.ys.notifier.application.event;

import com.ys.notifier.domain.event.NotifierEvent;
import com.ys.shared.event.DomainEvent;
import com.ys.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotifierEventPublisher implements DomainEventPublisher<NotifierEvent> {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(String eventType, NotifierEvent payload, LocalDateTime publishedAt) {
        //PayloadInfo payloadInfo = PayloadInfoStore.THREAD_LOCAL.get();
        String publisherId = "SYSTEM";
        eventPublisher.publishEvent(DomainEvent.create(
                eventType, payload, publisherId, publishedAt));
    }
}
