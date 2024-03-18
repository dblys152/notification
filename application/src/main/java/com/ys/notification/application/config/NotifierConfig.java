package com.ys.notification.application.config;

import com.ys.notification.domain.command.CoolSmsNotifier;
import com.ys.notification.domain.command.DefaultNotifierFinder;
import com.ys.notification.domain.command.EmailNotifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class NotifierConfig {
    @Value("${spring.profiles.active}")
    private String ACTIVE_PROFILE;
    @Value("${aws-mail.from}")
    private String EMAIL_FROM;
    @Value("${test-to-mail}")
    private String TEST_TO_MAIL;
    @Value("${cool-sms.from}")
    private String COOL_SMS_FROM;
    @Value("${cool-sms.key}")
    private String COOL_SMS_API_KEY;
    @Value("${cool-sms.secret}")
    private String COOL_SMS_API_SECRET_KEY;
    @Value("${test-to-mobile}")
    private String TEST_TO_MOBILE;

    @Bean
    public EmailNotifier emailNotifier(JavaMailSender javaMailSender) {
        return new EmailNotifier(ACTIVE_PROFILE, EMAIL_FROM, javaMailSender, TEST_TO_MAIL);
    }

    @Bean
    public CoolSmsNotifier coolSmsNotifier() {
        return new CoolSmsNotifier(ACTIVE_PROFILE, COOL_SMS_FROM, COOL_SMS_API_KEY, COOL_SMS_API_SECRET_KEY, TEST_TO_MOBILE);
    }

    @Bean
    public DefaultNotifierFinder defaultNotifierFinder(EmailNotifier emailNotifier, CoolSmsNotifier coolSmsNotifier) {
        return new DefaultNotifierFinder(
                emailNotifier,
                coolSmsNotifier
        );
    }
}
