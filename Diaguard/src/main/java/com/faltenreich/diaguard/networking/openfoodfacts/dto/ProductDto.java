package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class ProductDto {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    // Workaround: Sometimes a Number, sometimes a String
    @SerializedName("sortkey")
    public JsonElement identifier;

    @SerializedName("lang")
    public String languageCode;

    @SerializedName("product_name")
    public String name;

    @SerializedName("brands")
    public String brand;

    @SerializedName("ingredients_text")
    public String ingredients;

    @SerializedName("labels")
    public String labels;

    @SerializedName("nutriments")
    public NutrientsDto nutrients;

    @SerializedName("last_edit_dates_tags")
    public String[] lastEditDates;

    public boolean isValid() {
        boolean hasName = !StringUtils.isBlank(name);
        boolean hasNutrients = nutrients != null && nutrients.carbohydrates != null;
        return hasName && hasNutrients;
    }
}
