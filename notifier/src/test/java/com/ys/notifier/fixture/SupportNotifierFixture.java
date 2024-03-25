package com.ys.notifier.fixture;

import com.ys.notification.domain.Destination;
import com.ys.notification.domain.NotificationId;
import com.ys.notification.domain.NotificationType;

public class SupportNotifierFixture {
    protected static final NotificationId NOTIFICATION_ID = NotificationId.of(9999L);
    protected static final Destination DESTINATION_EMAIL = Destination.of(NotificationType.EMAIL, "test@mail.com");
    protected static final Destination DESTINATION_SMS = Destination.of(NotificationType.COOL_SMS, "010-7777-8888");
    protected static final String TITLE = "(Web 발신)";
    protected static final String CONTENTS = "테스트 알림";
    protected static final String ACTIVE_PROFILE = "test";
    protected static final String MAIL_FROM = "ys@gmail.com";
    protected static final String TEST_TO_MAIL = "testys@mail.com";
    protected static final String SMS_FROM = "0277778888";
    protected static final String TEST_TO_MOBILE = "010-6666-5555";
}
