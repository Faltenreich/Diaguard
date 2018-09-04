package com.faltenreich.diaguard.data.async;

public interface DataLoaderListener<T> {
    T onShouldLoad();
    void onDidLoad(T data);
}
