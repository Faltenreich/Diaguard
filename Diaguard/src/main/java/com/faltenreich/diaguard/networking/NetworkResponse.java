package com.faltenreich.diaguard.networking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NetworkResponse<T> {

    @NonNull
    private int statusCode;

    @Nullable
    private T data;

    NetworkResponse(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    NetworkResponse(int statusCode) {
        this(statusCode, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getData() {
        return data;
    }
}
