package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.NetworkServer;
import com.faltenreich.diaguard.util.Helper;

public class OpenFoodFactsServer extends NetworkServer<OpenFoodFactsApi> {

    private static final String HOST_FORMAT = "https://world-%s.openfoodfacts.org";

    public OpenFoodFactsServer() {
        super(OpenFoodFactsApi.class);
    }

    @Override
    public String getBaseUrl() {
        return String.format(HOST_FORMAT, Helper.getLanguageCode());
    }
}
