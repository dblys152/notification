package com.ys.notifier.domain;

import com.ys.notification.domain.NotificationType;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Slf4j
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
    protected NotifierResult _execute(ExecuteNotifierCommand factor) {
        Message message = new Message();
        String destination = factor.getDestination().getValue();
        if (isTestProfile()) {
            destination = testToMobile;
        }
        message.setFrom(from);
        message.setTo(destination.replaceAll("-", ""));
        message.setText(factor.getContents());

        try {
            nurigoMessageService.send(message);

            return DefaultNotifierResult.success(factor.getNotificationId());
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException ex) {
            log.error(ex.getMessage());

            return DefaultNotifierResult.fail(factor.getNotificationId());
        }
    }
}
