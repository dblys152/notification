package com.ys.notification.application.config;

import com.ys.notification.domain.command.notifier.CoolSmsNotifier;
import com.ys.notification.domain.command.notifier.DefaultNotifierFinder;
import com.ys.notification.domain.command.notifier.EmailNotifier;
import net.nurigo.sdk.NurigoApp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

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
    @Value("${cool-sms.uri}")
    private String COOL_SMS_URI;
    @Value("${test-to-mobile}")
    private String TEST_TO_MOBILE;

    @Bean
    public DefaultNotifierFinder defaultNotifierFinder(JavaMailSender javaMailSender) {
        return new DefaultNotifierFinder(List.of(
                new EmailNotifier(ACTIVE_PROFILE, EMAIL_FROM, javaMailSender, TEST_TO_MAIL),
                new CoolSmsNotifier(
                        ACTIVE_PROFILE,
                        COOL_SMS_FROM,
                        NurigoApp.INSTANCE.initialize(COOL_SMS_API_KEY, COOL_SMS_API_SECRET_KEY, COOL_SMS_URI),
                        TEST_TO_MOBILE
                ))
        );
    }
}
