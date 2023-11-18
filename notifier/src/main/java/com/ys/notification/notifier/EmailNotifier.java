package com.ys.notification.notifier;

import com.ys.notification.domain.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@RequiredArgsConstructor
public class EmailNotifier implements Notifier {
    private final String activeProfile;
    private final String from;
    private final JavaMailSender mailSender;
    private final String testToMail;

    @Override
    public void execute(Notification notification) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String destination = notification.getDestination().getValue();
            if (activeProfile.equals("dev") || activeProfile.equals("test")) {
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
