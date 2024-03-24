package com.ys.notification.application.usecase;

import com.ys.notification.domain.Notification;
import com.ys.notification.domain.ProcessSendingResultCommand;

public interface ProcessSendingResultUseCase {
    Notification processSendingResult(ProcessSendingResultCommand command);
}
