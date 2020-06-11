package com.faltenreich.diaguard.shared.data.async;

public interface DataCallback<T> {
    void onResult(T result);
}
