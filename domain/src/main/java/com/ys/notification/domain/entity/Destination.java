package com.ys.notification.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.apache.commons.validator.routines.EmailValidator;

@Value(staticConstructor = "of")
public class Destination {
    private static final String MOBILE_REGEX = "\\d{3,4}-\\d{4}-\\d{4}";

    @NotBlank
    @Size(min = 1, max = 30)
    String value;

    public void validate(NotificationType type) {
        if (type.equals(NotificationType.EMAIL)) {
            validateEmail();
        } else if (type.equals(NotificationType.COOL_SMS)) {
            validateMobile();
        }
    }

    private void validateEmail() {
        if (!EmailValidator.getInstance().isValid(this.value)) {
            throw new IllegalArgumentException("이메일 패턴이 올바르지 않습니다.");
        }
    }

    private void validateMobile() {
        if (!this.value.matches(MOBILE_REGEX)) {
            throw new IllegalArgumentException("모바일 번호 패턴이 올바르지 않습니다.");
        }
    }
}
