package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.util.Helper;

import retrofit.Endpoint;

/**
 * Created by Faltenreich on 08.11.2016.
 */

class OpenFoodFactEndpoint implements Endpoint {

    private static final String HOST_FORMAT = "https://world-%s.openfoodfacts.org";

    @Override
    public String getUrl() {
        return getHost();
    }

    @Override
    public String getName() {
        return getHost();
    }

    static String getHost() {
        return String.format(HOST_FORMAT, Helper.getCountryCode());
    }
}
