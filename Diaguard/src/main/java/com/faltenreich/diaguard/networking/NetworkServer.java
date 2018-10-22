package com.faltenreich.diaguard.networking;

import retrofit2.Retrofit;

abstract public class NetworkServer<API> {

    public API api;

    public NetworkServer(Class<API> clazz) {
        this.api = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .build()
                .create(clazz);
    }

    public abstract String getBaseUrl();
}
