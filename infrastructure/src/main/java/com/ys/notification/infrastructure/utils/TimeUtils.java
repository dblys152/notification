package com.ys.notification.infrastructure.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class TimeUtils {
    public static LocalDate getFirstDate(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate getLastDate(int year, Month month) {
        return LocalDate.of(year, month, 1)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime getFirstDateTime(int year, Month month) {
        return getFirstDate(year, month).atStartOfDay();
    }

    public static LocalDateTime getLastDateTime(int year, Month month) {
        return getLastDate(year, month).atTime(LocalTime.MAX);
    }
}
