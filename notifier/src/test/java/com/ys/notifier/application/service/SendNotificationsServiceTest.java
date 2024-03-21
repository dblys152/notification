package com.ys.notifier.application.service;

import com.ys.notification.domain.Notification;
import com.ys.notification.domain.NotificationStatus;
import com.ys.notification.domain.Notifications;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendNotificationsServiceTest {
    @InjectMocks
    private SendNotificationsService sut;

    @Mock
    private RecordNotificationPort recordNotificationPort;
    @Mock
    private LoadNotificationPort loadNotificationPort;
    @Mock
    private DefaultNotifierFinder defaultNotifierFinder;



    @Test
    void 예약된_알림을_일괄_발송한다() {
        Notification emailNotification = mock(Notification.class);
        Notification coolSmsNotification = mock(Notification.class);
        Notifications notificationEntities = Notifications.of(List.of(emailNotification, coolSmsNotification));

        given(loadNotificationPort.findAllByStatusAndSentAtLessThanEqual(eq(NotificationStatus.RESERVED), any(LocalDateTime.class))).willReturn(notificationEntities);
        given(defaultNotifierFinder.getNotifier(emailNotification)).willReturn(mock(EmailNotifier.class));

        for(Notification notification : notificationEntities.getItems()) {
            Notifier notifier = mock(Notifier.class);
            when(defaultNotifierFinder.getNotifier(notification)).thenReturn(notifier);
        }

        Notifications actual = sut.sendReservedNotifications();

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(loadNotificationPort).should().findAllByStatusAndSentAtLessThanEqual(eq(NotificationStatus.RESERVED), any(LocalDateTime.class)),
                () -> then(recordNotificationPort).should().saveAll(notificationEntities)
        );
    }

    @Test
    void 예약된_알림을_일괄_발송_시_예외가_발생하면_FAILED_상태로_처리한다() {
        Notification emailNotification = mock(Notification.class);
        Notification coolSmsNotification = mock(Notification.class);
        Notifications notificationEntities = Notifications.of(List.of(emailNotification, coolSmsNotification));

        given(loadNotificationPort.findAllByStatusAndSentAtLessThanEqual(eq(NotificationStatus.RESERVED), any(LocalDateTime.class))).willReturn(notificationEntities);
        given(defaultNotifierFinder.getNotifier(emailNotification)).willReturn(mock(EmailNotifier.class));

        for(Notification notification : notificationEntities.getItems()) {
            Notifier notifier = mock(Notifier.class);
            given(defaultNotifierFinder.getNotifier(notification)).willReturn(notifier);

            doThrow(new IllegalStateException()).when(notifier).execute(notification);
        }

        Notifications actual = sut.sendReservedNotifications();

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(loadNotificationPort).should().findAllByStatusAndSentAtLessThanEqual(eq(NotificationStatus.RESERVED), any(LocalDateTime.class)),
                () -> then(recordNotificationPort).should().saveAll(notificationEntities)
        );
    }
}