package com.ys.notification.domain;

import com.ys.shared.exception.BadRequestException;
import com.ys.shared.utils.SelfValidating;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProcessSendingResultCommand extends SelfValidating<ProcessSendingResultCommand> {
    @Valid
    @NotNull
    private NotificationId notificationId;

    @NotNull
    private NotificationStatus status;

    public ProcessSendingResultCommand(NotificationId notificationId, NotificationStatus status) {
        this.notificationId = notificationId;
        this.status = status;
        validateSelf();
        validateStatus();
    }

    public void validateStatus() {
        if (!(this.status.isSucceeded() || this.status.isFailed())) {
            throw new BadRequestException("성공 혹은 실패만 처리 가능합니다.");
        }
    }
}
