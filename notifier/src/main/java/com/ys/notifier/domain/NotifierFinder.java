package com.ys.notifier.domain;

public interface NotifierFinder<FACTOR, RESULT> {
    Notifier<FACTOR, RESULT> getNotifier(FACTOR factor);
}
