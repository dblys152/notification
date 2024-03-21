package com.ys.notification.adapter.infrastructure.persistence.fixture;

import com.ys.notification.domain.*;

import java.time.LocalDateTime;

public class SupportNotificationFixture {
    protected static final LocalDateTime NOW = LocalDateTime.now();
    protected static final NotificationId NOTIFICATION_ID = NotificationId.of(999999999L);
    protected static final NotificationId NOTIFICATION_ID2 = NotificationId.of(999999998L);
    protected static final LocalDateTime SENT_AT = NOW;
    protected static final Destination DESTINATION_EMAIL = Destination.of(NotificationType.EMAIL, "test@mail.com");
    protected static final Destination DESTINATION_COOL_SMS = Destination.of(NotificationType.COOL_SMS, "010-7777-8888");
    protected static final String TITLE = "TITLE";
    protected static final String CONTENTS = "CONTENTS";
    protected static final Sender SENDER = Sender.of(SenderType.SYSTEM, null);
    protected static final Receiver RECEIVER = Receiver.of(ReceiverType.USER, "534324");

    protected Notification getNotificationByStatus(NotificationId notificationId, NotificationStatus status) {
        return Notification.of(
                notificationId, NotificationType.EMAIL, status, SENT_AT, DESTINATION_EMAIL, TITLE, CONTENTS, SENDER, RECEIVER, NOW, NOW);
    }
}
