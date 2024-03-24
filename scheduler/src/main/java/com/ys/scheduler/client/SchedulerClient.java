package com.ys.scheduler.client;

import com.ys.scheduler.api.NotificationApiFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerClient {
    private final NotificationApiFeignClient notificationApiFeignClient;

    @Scheduled(fixedRate = 10000) // 10초 마다 실행
    public void runNotifier() {
        notificationApiFeignClient.changeReservedToWaiting();
        log.info("예약 알림 발송 API 호출.");
    }
}
