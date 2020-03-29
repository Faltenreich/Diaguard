package com.faltenreich.diaguard.feature.food.networking.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponseDto {

    @SerializedName("count")
    public int count;

    @SerializedName("products")
    public List<ProductDto> products;
}
