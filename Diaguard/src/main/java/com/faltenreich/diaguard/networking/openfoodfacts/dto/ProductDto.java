package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.networking.NetworkDto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class ProductDto extends NetworkDto {

    @SerializedName("sortkey")
    public int identifier;

    @SerializedName("product_name")
    public String name;

    @SerializedName("generic_name")
    public String fullName;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("brands")
    public String brand;

    @SerializedName("ingredients_text")
    public String ingredients;

    @SerializedName("labels")
    public String labels;

    @SerializedName("nutriments")
    public NutrientsDto nutrients;

    @SerializedName("nutrient_levels")
    public NutrientLevelsDto nutrientLevels;

    public boolean isValid() {
        boolean hasName = name != null && name.length() > 0;
        float nutrition = nutrients.carbohydrates +
                nutrients.energy +
                nutrients.fat +
                nutrients.fatSaturated +
                nutrients.fiber +
                nutrients.proteins +
                nutrients.salt +
                nutrients.sodium +
                nutrients.sugar;
        boolean hasNutrition = nutrition > 0;
        return hasName && hasNutrition;
    }
}
