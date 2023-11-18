package com.ys.notification.notifier;

import com.ys.notification.domain.Notification;
import jakarta.mail.MessagingException;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;

public interface Notifier {
    void execute(Notification notification);
}
