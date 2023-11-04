package com.ys.notification.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class DestinationTest {
    @ParameterizedTest(name = "email : {0}")
    @MethodSource("getWrongEmails")
    void 이메일_알림의_목적지_검증_시_이메일_형식이_맞지_않으면_에러를_반환한다(String wrongEmail) {
        Destination destination = Destination.of(wrongEmail);

        assertThatThrownBy(() -> destination.validate(NotificationType.EMAIL)).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getWrongEmails() {
        return Stream.of(
                Arguments.arguments("test@mail", 1),
                Arguments.arguments("testmail", 2),
                Arguments.arguments("testmail@", 3),
                Arguments.arguments("testmail@mail.", 4)
        );
    }

    @ParameterizedTest(name = "mobile : {0}")
    @MethodSource("getWrongMobiles")
    void SMS_알림의_목적지_검증_시_모바일_형식이_맞지_않으면_에러를_반환한다(String wrongMobile) {
        Destination destination = Destination.of(wrongMobile);

        assertThatThrownBy(() -> destination.validate(NotificationType.COOL_SMS)).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getWrongMobiles() {
        return Stream.of(
                Arguments.arguments("010-12341-234", 1),
                Arguments.arguments("01077778888", 2),
                Arguments.arguments("010-77778888", 3)
        );
    }
}