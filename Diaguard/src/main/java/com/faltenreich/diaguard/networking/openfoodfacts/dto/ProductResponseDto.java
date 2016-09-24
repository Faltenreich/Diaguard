package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class ProductResponseDto extends OpenFoodFactsDto {

    @SerializedName("product")
    public ProductDto product;
}
