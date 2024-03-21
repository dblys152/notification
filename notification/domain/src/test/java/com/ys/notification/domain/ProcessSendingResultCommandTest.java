package com.ys.notification.domain;

import com.ys.notification.domain.fixture.SupportNotificationFixture;
import com.ys.shared.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProcessSendingResultCommandTest extends SupportNotificationFixture {
    @Test
    void 성공_혹은_실패_상태만_처리할_수_있다() {
        assertThatThrownBy(() -> new ProcessSendingResultCommand(NOTIFICATION_ID, NotificationStatus.RESERVED))
                .isInstanceOf(BadRequestException.class);
    }
}