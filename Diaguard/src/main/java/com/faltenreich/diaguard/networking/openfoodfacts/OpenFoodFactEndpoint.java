package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.data.PreferenceHelper;

import retrofit.Endpoint;

/**
 * Created by Faltenreich on 08.11.2016.
 */

public class OpenFoodFactEndpoint implements Endpoint {

    public static final String HOST_FORMAT = "http://world-%s.openfoodfacts.org";
    public static final String HOST_DEFAULT = String.format(HOST_FORMAT, "world");

    @Override
    public String getUrl() {
        return String.format(HOST_FORMAT, getCountryCode());
    }

    @Override
    public String getName() {
        return getCountryCode();
    }

    private String getCountryCode() {
        return PreferenceHelper.getInstance().getLocale().getCountry();
    }
}
