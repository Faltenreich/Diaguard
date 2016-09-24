package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.networking.NetworkDto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class OpenFoodFactsDto extends NetworkDto {

    @SerializedName("status")
    public int statusCode;

    @SerializedName("status_verbose")
    public String status;

    @SerializedName("code")
    public String code;
}
