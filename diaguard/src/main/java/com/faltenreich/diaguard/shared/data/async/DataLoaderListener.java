package com.faltenreich.diaguard.shared.data.async;

public interface DataLoaderListener<T> {
    T onShouldLoad();
    void onDidLoad(T data);
}
