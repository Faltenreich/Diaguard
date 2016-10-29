package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.networking.NetworkDto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class NutrientsDto extends NetworkDto {

    @SerializedName("carbohydrates_100g")
    public float carbohydrates;

    @SerializedName("energy_100g")
    public float energy;

    @SerializedName("fat_100g")
    public float fat;

    @SerializedName("saturated-fat_100g")
    public float fatSaturated;

    @SerializedName("fiber_100g")
    public float fiber;

    @SerializedName("proteins_100g")
    public float proteins;

    @SerializedName("salt_100g")
    public float salt;

    @SerializedName("sodium_100g")
    public float sodium;

    @SerializedName("sugars_100g")
    public float sugar;
}
