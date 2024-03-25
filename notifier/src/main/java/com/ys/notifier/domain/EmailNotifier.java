package com.ys.notifier.domain;

import com.ys.notification.domain.NotificationType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
public class EmailNotifier extends AbstractNotifier {
    private final String from;
    private final JavaMailSender mailSender;
    private final String testToMail;

    public EmailNotifier(String activeProfile, String from, JavaMailSender mailSender, String testToMail) {
        super(NotificationType.EMAIL, activeProfile);
        this.from = from;
        this.mailSender = mailSender;
        this.testToMail = testToMail;
    }

    @Override
    protected NotifierResult _execute(ExecuteNotifierCommand factor) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String destination = factor.getDestination().getValue();
            if (isTestProfile()) {
                destination = testToMail;
            }
            helper.setFrom(from);
            helper.setTo(destination);
            helper.setSubject(factor.getTitle());
            helper.setText(factor.getContents(), true);

            mailSender.send(message);

            return DefaultNotifierResult.success(factor.getNotificationId());
        } catch (MessagingException | MailException ex) {
            log.error(ex.getMessage());

            return DefaultNotifierResult.fail(factor.getNotificationId());
        }
    }
}
