package com.ys.notification.application.usecase;

import com.ys.notification.domain.Notifications;
import com.ys.notification.domain.ProcessSendingResultCommand;

import java.util.List;

public interface ProcessSendingResultBulkUseCase {
    Notifications processSendingResults(List<ProcessSendingResultCommand> commandList);
}
