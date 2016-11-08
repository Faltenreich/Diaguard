package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.networking.NetworkDto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class NutrientsDto extends NetworkDto {

    @SerializedName("carbohydrates_100g")
    public Float carbohydrates;

    @SerializedName("energy_100g")
    public Float energy;

    @SerializedName("fat_100g")
    public Float fat;

    @SerializedName("saturated-fat_100g")
    public Float fatSaturated;

    @SerializedName("fiber_100g")
    public Float fiber;

    @SerializedName("proteins_100g")
    public Float proteins;

    @SerializedName("salt_100g")
    public Float salt;

    @SerializedName("sodium_100g")
    public Float sodium;

    @SerializedName("sugars_100g")
    public Float sugar;
}
