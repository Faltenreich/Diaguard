package com.faltenreich.diaguard.shared.data.primitive;

public interface Consumer<T> {

    void accept(T t);
}