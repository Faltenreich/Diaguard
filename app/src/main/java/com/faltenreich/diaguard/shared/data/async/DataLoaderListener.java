package com.faltenreich.diaguard.shared.data.async;

import android.content.Context;

public interface DataLoaderListener<T> {
    T onShouldLoad(Context context);
    void onDidLoad(T data);
}
