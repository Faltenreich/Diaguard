package com.faltenreich.diaguard.networking.google;

import com.faltenreich.diaguard.networking.NetworkService;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class GoogleImageSearchService extends NetworkService<GoogleImageSearchApi> {

    public GoogleImageSearchService() {
        super(GoogleImageSearchApi.class);
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.117 Safari/537.36");
            }
        });
        GsonBuilder gsonBuilder = new GsonBuilder();
        builder.setConverter(new GsonConverter(gsonBuilder.create()));
        builder.setEndpoint(getServerUrl());
        builder.setLogLevel(RestAdapter.LogLevel.NONE);
        return builder;
    }

    @Override
    protected String getServerUrl() {
        return "https://www.google.com";
    }
}
