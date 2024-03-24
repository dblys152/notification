package com.ys.notification.domain;

import com.ys.notification.domain.event.NotificationBulkEvent;
import com.ys.notification.domain.fixture.SupportNotificationFixture;
import com.ys.shared.event.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class NotificationsTest extends SupportNotificationFixture {
    private Notifications reservedNotifications;
    private Notifications waitingNotifications;

    @BeforeEach
    void setUp() {
        reservedNotifications = Notifications.of(List.of(
                getNotificationByStatus(NOTIFICATION_ID, NotificationStatus.RESERVED), getNotificationByStatus(NOTIFICATION_ID2, NotificationStatus.RESERVED)));
        waitingNotifications = Notifications.of(List.of(
                getNotificationByStatus(NOTIFICATION_ID, NotificationStatus.WAITING), getNotificationByStatus(NOTIFICATION_ID2, NotificationStatus.WAITING)));

    }

    @Test
    void 대기중_상태로_일괄_변경한다() {
        reservedNotifications.toWaiting();

        assertThat(reservedNotifications.getItems().stream()
                .filter(notification -> !notification.getStatus().isWaiting())
                .count()).isEqualTo(0);
    }

    @Test
    void 전송_결과를_일괄_처리한다() {
        List<ProcessSendingResultCommand> commandList = List.of(
                new ProcessSendingResultCommand(NOTIFICATION_ID, NotificationStatus.SUCCEEDED),
                new ProcessSendingResultCommand(NOTIFICATION_ID2, NotificationStatus.FAILED));

        Notifications actual = waitingNotifications.processSendingResults(commandList);

        assertThat(actual.isEmpty()).isFalse();
    }

    @Test
    void 전송_결과_일괄_처리_시_대기중_알림이_아니면_반환하지_않는다() {
        List<ProcessSendingResultCommand> commandList = List.of(
                new ProcessSendingResultCommand(NOTIFICATION_ID, NotificationStatus.SUCCEEDED),
                new ProcessSendingResultCommand(NOTIFICATION_ID2, NotificationStatus.FAILED));

        Notifications actual = reservedNotifications.processSendingResults(commandList);

        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void 타입별_이벤트를_발행한다() {
        DomainEventPublisher<NotificationBulkEvent> domainEventPublisher = mock(DomainEventPublisher.class);

        waitingNotifications.eventPublish(domainEventPublisher);
    }
}