package com.faltenreich.diaguard.feature.food.networking;

import com.faltenreich.diaguard.shared.networking.NetworkServer;
import com.faltenreich.diaguard.shared.Helper;

public class OpenFoodFactsServer extends NetworkServer<OpenFoodFactsApi> {

    OpenFoodFactsServer() {
        super(OpenFoodFactsApi.class);
    }

    @Override
    public String getBaseUrl() {
        String languageCode = Helper.getLanguageCode();
        // Special case: "en" must be stripped for api to work
        if (languageCode.equals("en")) {
            return "https://world.openfoodfacts.org/";
        } else {
            return String.format("https://world-%s.openfoodfacts.org/", languageCode);
        }
    }
}
