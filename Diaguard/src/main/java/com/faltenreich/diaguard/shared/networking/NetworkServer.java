package com.faltenreich.diaguard.shared.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

abstract public class NetworkServer<API> {

    public API api;

    public NetworkServer(Class<API> clazz) {
        this.api = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(clazz);
    }

    public abstract String getBaseUrl();
}
