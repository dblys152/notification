package com.ys.notification.infrastructure.utils;

public interface CommandFactory<REQUEST, COMMAND> {

    COMMAND create(REQUEST request);
}
