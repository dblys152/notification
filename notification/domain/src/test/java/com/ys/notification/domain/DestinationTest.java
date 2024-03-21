package com.ys.notification.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DestinationTest {
    @ParameterizedTest(name = "email : {0}")
    @MethodSource("getWrongEmails")
    void 이메일_목적지_생성_시_이메일_형식이_맞지_않으면_에러를_반환한다(String wrongEmail) {
        assertThatThrownBy(() -> Destination.of(NotificationType.EMAIL, wrongEmail))
                .isInstanceOf(IllegalArgumentException.class);
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
    void SMS_목적지_생성_시_모바일_형식이_맞지_않으면_에러를_반환한다(String wrongMobile) {
        assertThatThrownBy(() -> Destination.of(NotificationType.COOL_SMS, wrongMobile))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getWrongMobiles() {
        return Stream.of(
                Arguments.arguments("010-12341-234", 1),
                Arguments.arguments("01077778888", 2),
                Arguments.arguments("010-77778888", 3)
        );
    }
}