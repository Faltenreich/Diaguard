package com.faltenreich.diaguard.networking;

import androidx.annotation.Nullable;

public class NetworkResponse<T> {

    private int statusCode;

    @Nullable
    private T data;

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
