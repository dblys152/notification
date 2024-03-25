package com.ys.notifier.domain;

import com.ys.notification.domain.NotificationType;
import com.ys.notifier.fixture.SupportNotifierFixture;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class CoolSmsNotifierTest extends SupportNotifierFixture {
    private CoolSmsNotifier coolSmsNotifier;
    private DefaultMessageService defaultMessageService;

    @BeforeEach
    void setUp() {
        defaultMessageService = mock(DefaultMessageService.class);
        coolSmsNotifier = new CoolSmsNotifier(ACTIVE_PROFILE, SMS_FROM, defaultMessageService, TEST_TO_MOBILE);
    }

    @Test
    void SMS를_발송한다() {
        ExecuteNotifierCommand command = new ExecuteNotifierCommand(NOTIFICATION_ID, NotificationType.EMAIL, DESTINATION_EMAIL, TITLE, CONTENTS);

        NotifierResult actual = coolSmsNotifier.execute(command);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(defaultMessageService).should().send(any(Message.class))
        );
    }

    @Test
    void SMS_발송_실패_시_FAIL_RESULT를_반환한다() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        ExecuteNotifierCommand command = new ExecuteNotifierCommand(NOTIFICATION_ID, NotificationType.EMAIL, DESTINATION_EMAIL, TITLE, CONTENTS);

        doThrow(new NurigoMessageNotReceivedException("Test exception")).when(defaultMessageService).send(any(Message.class));

        NotifierResult actual = coolSmsNotifier.execute(command);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(defaultMessageService).should().send(any(Message.class))
        );
    }
}