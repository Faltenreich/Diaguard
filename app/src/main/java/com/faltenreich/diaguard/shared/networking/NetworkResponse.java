package com.faltenreich.diaguard.shared.networking;

import androidx.annotation.Nullable;

public class NetworkResponse<T> {

    private final int statusCode;

    @Nullable
    private final T data;

    NetworkResponse(int statusCode, @Nullable T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    NetworkResponse(int statusCode) {
        this(statusCode, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Nullable
    public T getData() {
        return data;
    }
}
