package com.ys.scheduler.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;

@FeignClient(name = "notification-api",
        url = "${base-host}${endpoint.notification}")
public interface NotificationApiFeignClient {
    @PatchMapping(value = "/reserved-to-waiting", headers = {"${api-key.header}=${api-key.value.notification}", "Content-Type=application/json"})
    void changeReservedToWaiting();
}
