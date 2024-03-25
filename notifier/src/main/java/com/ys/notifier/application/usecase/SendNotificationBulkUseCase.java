package com.ys.notifier.application.usecase;

import com.ys.notifier.domain.ExecuteNotifierCommand;

import java.util.List;

public interface SendNotificationBulkUseCase {
    void send(List<ExecuteNotifierCommand> commandList);
}
