package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.networking.NetworkDto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Faltenreich on 25.09.2016.
 */

public class SearchResponseDto extends NetworkDto {

    @SerializedName("products")
    public List<ProductDto> products;
}
