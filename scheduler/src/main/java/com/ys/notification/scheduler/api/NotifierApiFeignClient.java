package com.ys.notification.scheduler.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "notification-api",
        url = "${base-host}${endpoint.notifier-module.notification}")
public interface NotifierApiFeignClient {
    @PostMapping(value = "/reserved-sending", headers = {"${api-key.header}=${api-key.value.notification}", "Content-Type=application/json"})
    void sendAll();
}
