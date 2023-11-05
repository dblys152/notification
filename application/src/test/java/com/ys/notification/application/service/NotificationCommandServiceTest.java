package com.ys.notification.application.service;

import com.ys.notification.application.port.out.RecordNotificationPort;
import com.ys.notification.domain.CreateNotificationCommand;
import com.ys.notification.domain.Destination;
import com.ys.notification.domain.Notification;
import com.ys.notification.domain.NotificationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class NotificationCommandServiceTest {
    private static final Destination DESTINATION_EMAIL = Destination.of("test@mail.com");

    @InjectMocks
    private NotificationCommandService sut;

    @Mock
    private RecordNotificationPort recordNotificationPort;

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
}