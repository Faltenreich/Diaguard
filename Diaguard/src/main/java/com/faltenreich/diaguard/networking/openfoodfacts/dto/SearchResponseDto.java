package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponseDto {
    @SerializedName("products")
    public List<ProductDto> products;
}
