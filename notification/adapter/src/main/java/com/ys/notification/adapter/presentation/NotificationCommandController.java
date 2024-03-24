package com.ys.notification.adapter.presentation;

import com.ys.notification.adapter.presentation.model.NotificationModel;
import com.ys.notification.application.usecase.ChangeReservedToWaitingUseCase;
import com.ys.notification.application.usecase.ReserveNotificationUseCase;
import com.ys.notification.application.usecase.model.ReserveNotificationRequest;
import com.ys.notification.domain.CreateNotificationCommand;
import com.ys.notification.domain.Notification;
import com.ys.notification.domain.Notifications;
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
    private final CommandFactory<ReserveNotificationRequest, CreateNotificationCommand> createNotificationCommandFactory;
    private final ReserveNotificationUseCase reserveNotificationUseCase;
    private final ChangeReservedToWaitingUseCase changeReservedToWaitingUseCase;

    @PostMapping("")
    public ResponseEntity<ApiResponseModel<NotificationModel>> reserve(@RequestBody @Valid ReserveNotificationRequest request) {
        CreateNotificationCommand command = createNotificationCommandFactory.create(request);

        Notification notification = reserveNotificationUseCase.reserve(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseModel.success(HttpStatus.CREATED.value(), NotificationModel.fromDomain(notification)));
    }

    @PostMapping("/reserved-to-waiting")
    public ResponseEntity<ApiResponseModel<List<NotificationModel>>> changeReservedToWaiting() {
        Notifications notifications = changeReservedToWaitingUseCase.changeReservedToWaiting();

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseModel.success(HttpStatus.OK.value(), notifications.getItems().stream()
                        .map(NotificationModel::fromDomain)
                        .toList()));
    }
}
