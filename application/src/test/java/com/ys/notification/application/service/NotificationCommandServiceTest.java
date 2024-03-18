package com.ys.notification.application.service;

import com.ys.notification.application.port.out.LoadNotificationPort;
import com.ys.notification.application.port.out.RecordNotificationPort;
import com.ys.notification.domain.command.CreateNotificationCommand;
import com.ys.notification.domain.command.DefaultNotifierFinder;
import com.ys.notification.domain.command.EmailNotifier;
import com.ys.notification.domain.command.Notifier;
import com.ys.notification.domain.entity.*;
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
class NotificationCommandServiceTest {
    private static final Destination DESTINATION_EMAIL = Destination.of("test@mail.com");

    @InjectMocks
    private NotificationCommandService sut;

    @Mock
    private RecordNotificationPort recordNotificationPort;
    @Mock
    private LoadNotificationPort loadNotificationPort;
    @Mock
    private DefaultNotifierFinder defaultNotifierFinder;

    @Test
    void 알림을_예약한다() {
        CreateNotificationCommand command = mock(CreateNotificationCommand.class);
        given(command.getType()).willReturn(NotificationType.EMAIL);
        given(command.getDestination()).willReturn(DESTINATION_EMAIL);
        given(recordNotificationPort.save(any(Notification.class))).willReturn(mock(Notification.class));

        Notification actual = sut.reserve(command);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(recordNotificationPort).should().save(any(Notification.class))
        );
    }

    @Test
    void 예약된_알림을_일괄_발송한다() {
        Notification notification = mock(Notification.class);
        given(notification.getType()).willReturn(NotificationType.EMAIL);
        Notification notification2 = mock(Notification.class);
        given(notification2.getType()).willReturn(NotificationType.COOL_SMS);
        Notifications notificationEntities = Notifications.of(List.of(notification, notification2));

        given(loadNotificationPort.findAllByStatusAndSentAtLessThanEqual(eq(NotificationStatus.RESERVED), any(LocalDateTime.class))).willReturn(notificationEntities);
        given(defaultNotifierFinder.getNotifier(NotificationType.EMAIL)).willReturn(mock(EmailNotifier.class));

        for(Notification n : notificationEntities.getItems()) {
            Notifier notifier = mock(Notifier.class);
            when(defaultNotifierFinder.getNotifier(n.getType())).thenReturn(notifier);
        }

        Notifications actual = sut.sendAll();

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(loadNotificationPort).should().findAllByStatusAndSentAtLessThanEqual(eq(NotificationStatus.RESERVED), any(LocalDateTime.class)),
                () -> then(recordNotificationPort).should().saveAll(notificationEntities)
        );
    }

    @Test
    void 예약된_알림을_일괄_발송_시_예외가_발생하면_FAILED_상태로_처리한다() {
        Notification notification = mock(Notification.class);
        given(notification.getType()).willReturn(NotificationType.EMAIL);
        Notification notification2 = mock(Notification.class);
        given(notification2.getType()).willReturn(NotificationType.COOL_SMS);
        Notifications notificationEntities = Notifications.of(List.of(notification, notification2));

        given(loadNotificationPort.findAllByStatusAndSentAtLessThanEqual(eq(NotificationStatus.RESERVED), any(LocalDateTime.class))).willReturn(notificationEntities);
        given(defaultNotifierFinder.getNotifier(NotificationType.EMAIL)).willReturn(mock(EmailNotifier.class));

        for(Notification n : notificationEntities.getItems()) {
            Notifier notifier = mock(Notifier.class);
            given(defaultNotifierFinder.getNotifier(n.getType())).willReturn(notifier);

            doThrow(new IllegalStateException()).when(notifier).execute(n);
        }

        Notifications actual = sut.sendAll();

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(loadNotificationPort).should().findAllByStatusAndSentAtLessThanEqual(eq(NotificationStatus.RESERVED), any(LocalDateTime.class)),
                () -> then(recordNotificationPort).should().saveAll(notificationEntities)
        );
    }
}