package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.NetworkService;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class OpenFoodFactsService extends NetworkService<OpenFoodFactsApi> {

    public OpenFoodFactsService() {
        super(OpenFoodFactsApi.class);
    }

    @Override
    protected String getServerUrl() {
        return "http://world.openfoodfacts.org";
    }
}
