package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.NetworkService;
import com.faltenreich.diaguard.networking.NetworkTypeDoubleAdapter;
import com.faltenreich.diaguard.networking.NetworkTypeFloatAdapter;
import com.faltenreich.diaguard.networking.NetworkTypeIntegerAdapter;
import com.faltenreich.diaguard.networking.NetworkTypeStringAdapter;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

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
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(String.class, new NetworkTypeStringAdapter());
        gsonBuilder.registerTypeAdapter(Float.class, new NetworkTypeFloatAdapter());
        gsonBuilder.registerTypeAdapter(Double.class, new NetworkTypeDoubleAdapter());
        gsonBuilder.registerTypeAdapter(Integer.class, new NetworkTypeIntegerAdapter());
        builder.setConverter(new GsonConverter(gsonBuilder.create()));
        builder.setEndpoint(new OpenFoodFactEndpoint());
        return builder;
    }

    @Override
    protected String getServerUrl() {
        return OpenFoodFactEndpoint.getHost();
    }
}
