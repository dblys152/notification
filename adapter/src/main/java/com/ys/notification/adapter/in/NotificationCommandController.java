package com.ys.notification.adapter.in;

import com.ys.notification.application.port.in.ReserveNotificationRequest;
import com.ys.notification.application.port.in.ReserveNotificationUseCase;
import com.ys.notification.domain.CreateNotificationCommand;
import com.ys.notification.domain.Notification;
import com.ys.notification.infrastructure.utils.ApiResponseModel;
import com.ys.notification.infrastructure.utils.CommandFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/notifications",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NotificationCommandController {
    private final CommandFactory<ReserveNotificationRequest, CreateNotificationCommand> createNotificationCommandCommandFactory;
    private final ReserveNotificationUseCase reserveNotificationUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseModel<NotificationModel>> reserve(@RequestBody @Valid ReserveNotificationRequest request) {
        CreateNotificationCommand command = createNotificationCommandCommandFactory.create(request);

        Notification notification = reserveNotificationUseCase.reserve(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseModel.success(HttpStatus.CREATED.value(), NotificationModel.fromDomain(notification)));
    }
}
