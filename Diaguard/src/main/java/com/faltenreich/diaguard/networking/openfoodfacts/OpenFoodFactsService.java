package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.NetworkService;

import retrofit.RestAdapter;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class OpenFoodFactsService extends NetworkService<OpenFoodFactsApi> {

    public OpenFoodFactsService() {
        super(OpenFoodFactsApi.class);
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setEndpoint(new OpenFoodFactEndpoint());
        return builder;
    }

    @Override
    protected String getServerUrl() {
        return OpenFoodFactEndpoint.HOST_DEFAULT;
    }
}
