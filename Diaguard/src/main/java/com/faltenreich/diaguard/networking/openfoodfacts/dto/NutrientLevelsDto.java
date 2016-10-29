package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.data.entity.Food;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 29.10.2016.
 */

public class NutrientLevelsDto {

    @SerializedName("sugars")
    public Food.NutrientLevel sugar;

    @SerializedName("fat")
    public Food.NutrientLevel fat;

    @SerializedName("saturated-fat")
    public Food.NutrientLevel saturatedFat;

    @SerializedName("salt")
    public Food.NutrientLevel salt;
}
