package com.ys.notification.notifier;

import com.ys.notification.domain.Notification;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;

public class CoolSmsNotifier implements Notifier {
    private static final String NURIGO_APP_DOMAIN = "https://api.coolsms.co.kr";

    private final String activeProfile;
    private final String from;
    private final DefaultMessageService messageService;
    private final String testToMobile;

    public CoolSmsNotifier(String activeProfile, String from, String apiKey, String apiSecretKey, String testToMobile) {
        this.activeProfile = activeProfile;
        this.from = from;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, NURIGO_APP_DOMAIN);
        this.testToMobile = testToMobile;
    }

    @Override
    public void execute(Notification notification) {
        Message message = new Message();
        String destination = notification.getDestination().getValue();
        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            destination = testToMobile;
        }
        message.setFrom(from);
        message.setTo(destination.replaceAll("-", ""));
        message.setText(notification.getContents());

        try {
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}
