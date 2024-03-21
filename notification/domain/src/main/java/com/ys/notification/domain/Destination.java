package com.ys.notification.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Destination {
    private static final String MOBILE_REGEX = "\\d{3,4}-\\d{4}-\\d{4}";

    @NotBlank
    @Size(min = 1, max = 30)
    String value;

    public static Destination of(NotificationType notificationType, String destination) {
        validate(notificationType, destination);
        return new Destination(destination);
    }

    private static void validate(NotificationType notificationType, String destination) {
        switch (notificationType) {
            case EMAIL -> validateEmail(destination);
            case COOL_SMS -> validateMobile(destination);
            default -> throw new IllegalArgumentException("일치하는 유형이 없습니다.");
        }
    }

    private static void validateEmail(String destination) {
        if (!EmailValidator.getInstance().isValid(destination)) {
            throw new IllegalArgumentException("이메일 패턴이 올바르지 않습니다.");
        }
    }

    private static void validateMobile(String destination) {
        if (!destination.matches(MOBILE_REGEX)) {
            throw new IllegalArgumentException("모바일 번호 패턴이 올바르지 않습니다.");
        }
    }
}
