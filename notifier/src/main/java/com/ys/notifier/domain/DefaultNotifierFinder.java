package com.ys.notifier.domain;

import java.util.List;

public class DefaultNotifierFinder implements NotifierFinder<ExecuteNotifierCommand, NotifierResult> {
    private final List<Notifier<ExecuteNotifierCommand, NotifierResult>> candidates;

    public DefaultNotifierFinder(List<Notifier<ExecuteNotifierCommand, NotifierResult>> candidates) {
        this.candidates = candidates;
    }

    @Override
    public Notifier<ExecuteNotifierCommand, NotifierResult> getNotifier(ExecuteNotifierCommand factor) {
        return candidates.stream()
                .filter(p -> p.support(factor))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
