package com.ys.notification.domain;

import com.ys.notification.domain.command.CreateNotificationCommand;
import com.ys.notification.domain.entity.*;
import com.ys.notification.domain.fixture.SupportNotificationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationTest extends SupportNotificationFixture {
    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = Notification.of(
                NOTIFICATION_ID, NotificationType.EMAIL, NotificationStatus.RESERVED, SENT_AT, SenderType.SYSTEM, SENDER_USER_ID, DESTINATION_EMAIL, ReceiverType.USER, RECEIVER_ID, TITLE, CONTENTS, NOW, NOW, null);
    }

    @Test
    void 알림을_생성한다() {
        CreateNotificationCommand command = new CreateNotificationCommand(NotificationType.EMAIL, SENT_AT, SenderType.SYSTEM, SENDER_USER_ID, DESTINATION_EMAIL, ReceiverType.USER, RECEIVER_ID, TITLE, CONTENTS);

        Notification actual = Notification.create(command);

        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo(NotificationStatus.RESERVED);
    }

    @Test
    void 알림_생성_시_목적지_검증에_실패하면_에러를_반환한다() {
        CreateNotificationCommand command = new CreateNotificationCommand(NotificationType.COOL_SMS, SENT_AT, SenderType.SYSTEM, SENDER_USER_ID, DESTINATION_EMAIL, ReceiverType.USER, RECEIVER_ID, TITLE, CONTENTS);

        assertThatThrownBy(() -> Notification.create(command)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 알림_생성_시_목적지_검증에_실패하면_에러를_반환한다_2() {
        Destination wrongDestination = Destination.of("test@mail");
        CreateNotificationCommand command = new CreateNotificationCommand(NotificationType.EMAIL, SENT_AT, SenderType.SYSTEM, SENDER_USER_ID, wrongDestination, ReceiverType.USER, RECEIVER_ID, TITLE, CONTENTS);

        assertThatThrownBy(() -> Notification.create(command)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 알림_상태를_변경한다() {
        notification.changeStatus(NotificationStatus.SENT);

        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
    }

    @Test
    void 알림을_삭제한다() {
        notification.delete();

        assertThat(notification.getDeletedAt()).isNotNull();
    }
}