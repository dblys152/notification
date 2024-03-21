package com.ys.notification.domain;

import com.ys.notification.domain.fixture.SupportNotificationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class NotificationTest extends SupportNotificationFixture {
    private Notification reservedNotification;
    private Notification waitingNotification;
    private Notification failedNotification;

    @BeforeEach
    void setUp() {
        reservedNotification = getNotificationByStatus(NOTIFICATION_ID, NotificationStatus.RESERVED);
        waitingNotification = getNotificationByStatus(NOTIFICATION_ID, NotificationStatus.WAITING);
        failedNotification = getNotificationByStatus(NOTIFICATION_ID, NotificationStatus.FAILED);
    }

    @Test
    void 알림을_생성한다() {
        CreateNotificationCommand command = new CreateNotificationCommand(
                NotificationType.EMAIL, SENT_AT, DESTINATION_EMAIL, TITLE, CONTENTS, SENDER, RECEIVER);

        Notification actual = Notification.create(command);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getStatus()).isEqualTo(NotificationStatus.RESERVED),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getModifiedAt()).isNotNull()
        );
    }

    @Test
    void 대기중_상태로_변경한다() {
        reservedNotification.toWaiting();

        assertThat(reservedNotification.getStatus()).isEqualTo(NotificationStatus.WAITING);
        assertThat(reservedNotification.getModifiedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void 대기중_상태로_변경_시_예약된_상태가_아니면_에러를_반환한다() {
        assertThatThrownBy(() -> waitingNotification.toWaiting()).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> failedNotification.toWaiting()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 성공된_상태로_변경한다() {
        waitingNotification.succeed();

        assertThat(waitingNotification.getStatus()).isEqualTo(NotificationStatus.SUCCEEDED);
    }

    @Test
    void 성공된_상태로_변경_시_대기중_상태가_아니면_에러를_반환한다() {
        assertThatThrownBy(() -> reservedNotification.succeed()).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> failedNotification.succeed()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 실패된_상태로_변경한다() {
        waitingNotification.fail();

        assertThat(waitingNotification.getStatus()).isEqualTo(NotificationStatus.FAILED);
    }

    @Test
    void 실패된_상태로_변경_시_대기중_상태가_아니면_에러를_반환한다() {
        assertThatThrownBy(() -> reservedNotification.fail()).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> failedNotification.fail()).isInstanceOf(IllegalStateException.class);
    }
}