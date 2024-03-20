package com.ys.notification.domain.fixture;

import com.ys.notification.domain.Destination;
import com.ys.notification.domain.NotificationId;

import java.time.LocalDateTime;

public class SupportNotificationFixture {
    protected static final LocalDateTime NOW = LocalDateTime.now();
    protected static final NotificationId NOTIFICATION_ID = NotificationId.of(999999999L);
    protected static final LocalDateTime SENT_AT = NOW;
    protected static final String SENDER_USER_ID = null;
    protected static final Destination DESTINATION_EMAIL = Destination.of("test@mail.com");
    protected static final Destination DESTINATION_COOL_SMS = Destination.of("010-7777-8888");
    protected static final String RECEIVER_ID = "1";
    protected static final String TITLE = "TITLE";
    protected static final String CONTENTS = "CONTENTS";
}
