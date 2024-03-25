package com.ys.notifier.domain;

import com.ys.notification.domain.NotificationType;
import com.ys.notifier.fixture.SupportNotifierFixture;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class EmailNotifierTest extends SupportNotifierFixture {
    private EmailNotifier emailNotifier;
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailNotifier = new EmailNotifier(ACTIVE_PROFILE, MAIL_FROM, mailSender, TEST_TO_MAIL);
    }

    @Test
    void 이메일을_발송한다() throws MessagingException {
        ExecuteNotifierCommand command = new ExecuteNotifierCommand(NOTIFICATION_ID, NotificationType.EMAIL, DESTINATION_EMAIL, TITLE, CONTENTS);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        NotifierResult actual = emailNotifier.execute(command);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(mailSender).should().createMimeMessage(),
                () -> then(mailSender).should().send(mimeMessage)
        );
    }

    @Test
    void 이메일_발송_실패_시_FAIL_RESULT를_반환한다() throws MessagingException, jakarta.mail.MessagingException {
        ExecuteNotifierCommand command = new ExecuteNotifierCommand(NOTIFICATION_ID, NotificationType.EMAIL, DESTINATION_EMAIL, TITLE, CONTENTS);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        doThrow(new MailSendException("Test exception")).when(mailSender).send(mimeMessage);

        NotifierResult actual = emailNotifier.execute(command);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> then(mailSender).should().createMimeMessage(),
                () -> then(mailSender).should().send(mimeMessage)
        );
    }
}