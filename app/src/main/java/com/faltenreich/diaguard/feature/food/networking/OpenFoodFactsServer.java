package com.faltenreich.diaguard.feature.food.networking;

import com.faltenreich.diaguard.shared.networking.NetworkServer;

public class OpenFoodFactsServer extends NetworkServer<OpenFoodFactsApi> {

    OpenFoodFactsServer() {
        super(OpenFoodFactsApi.class);
    }

    @Override
    public String getBaseUrl() {
        return "https://world.openfoodfacts.org";
    }
}
