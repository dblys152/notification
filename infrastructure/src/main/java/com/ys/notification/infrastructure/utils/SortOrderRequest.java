package com.ys.notification.infrastructure.utils;

import lombok.Value;

@Value(staticConstructor = "of")
public class SortOrderRequest<T extends Enum> {
    T sortType;
    SortKeyword sortKeyword;
}
