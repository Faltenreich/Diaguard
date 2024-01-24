package com.faltenreich.diaguard.feature.food.networking.dto;

import com.google.gson.annotations.SerializedName;

public class NutrientsDto {

    @SerializedName("carbohydrates_100g")
    public Float carbohydrates;

    @SerializedName("energy-kcal_100g")
    public Float energyInKcal;

    @SerializedName("energy-kj_100g")
    public Float energyInKj;

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
