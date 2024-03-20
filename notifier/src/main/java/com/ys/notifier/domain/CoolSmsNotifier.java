package com.ys.notifier.domain;

import com.ys.notification.domain.entity.Notification;
import com.ys.notification.domain.entity.NotificationType;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

public class CoolSmsNotifier extends AbstractNotifier {
    private final String from;
    private final DefaultMessageService nurigoMessageService;
    private final String testToMobile;

    public CoolSmsNotifier(String activeProfile, String from, DefaultMessageService nurigoMessageService, String testToMobile) {
        super(NotificationType.COOL_SMS, activeProfile);
        this.from = from;
        this.nurigoMessageService = nurigoMessageService;
        this.testToMobile = testToMobile;
    }

    @Override
    protected void _execute(Notification notification) {
        Message message = new Message();
        String destination = notification.getDestination().getValue();
        if (isTestProfile()) {
            destination = testToMobile;
        }
        message.setFrom(from);
        message.setTo(destination.replaceAll("-", ""));
        message.setText(notification.getContents());

        try {
            nurigoMessageService.send(message);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}
