package com.ys.notifier.domain;

import com.ys.notification.domain.entity.Notification;
import com.ys.notification.domain.entity.NotificationType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

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
    protected void _execute(Notification notification) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String destination = notification.getDestination().getValue();
            if (isTestProfile()) {
                destination = testToMail;
            }
            helper.setFrom(from);
            helper.setTo(destination);
            helper.setSubject(notification.getTitle());
            helper.setText(notification.getContents(), true);

            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}
