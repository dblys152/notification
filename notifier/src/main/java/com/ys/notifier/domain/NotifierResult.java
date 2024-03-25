package com.ys.notifier.domain;

import com.ys.notifier.domain.event.NotifierEvent;
import com.ys.shared.event.DomainEventPublisher;

public interface NotifierResult {
    void publishEvent(DomainEventPublisher<NotifierEvent> domainEventPublisher);
}
