package com.ys.notification.adapter.in;

import com.ys.notification.application.port.in.ReserveNotificationRequest;
import com.ys.notification.application.port.in.ReserveNotificationUseCase;
import com.ys.notification.application.port.in.SendReservedNotificationsUseCase;
import com.ys.notification.domain.command.CreateNotificationCommand;
import com.ys.notification.domain.entity.Notification;
import com.ys.notification.domain.entity.Notifications;
import com.ys.shared.utils.ApiResponseModel;
import com.ys.shared.utils.CommandFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/notifications",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NotificationCommandController {
    private final CommandFactory<ReserveNotificationRequest, CreateNotificationCommand> createNotificationCommandCommandFactory;
    private final ReserveNotificationUseCase reserveNotificationUseCase;
    private final SendReservedNotificationsUseCase sendReservedNotificationsUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseModel<NotificationModel>> reserve(@RequestBody @Valid ReserveNotificationRequest request) {
        CreateNotificationCommand command = createNotificationCommandCommandFactory.create(request);

        Notification notification = reserveNotificationUseCase.reserve(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseModel.success(HttpStatus.CREATED.value(), NotificationModel.fromDomain(notification)));
    }

    @PostMapping("/reserved-sending")
    public ResponseEntity<ApiResponseModel<List<NotificationModel>>> sendAll() {
        Notifications notifications = sendReservedNotificationsUseCase.sendAll();

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseModel.success(HttpStatus.OK.value(), notifications.getItems().stream()
                        .map(NotificationModel::fromDomain)
                        .toList()));
    }
}
