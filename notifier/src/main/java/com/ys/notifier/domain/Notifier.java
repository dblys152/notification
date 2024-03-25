package com.ys.notifier.domain;

public interface Notifier<FACTOR, RESULT> {
    boolean support(FACTOR factor);
    RESULT execute(FACTOR factor);
}
