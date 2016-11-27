package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.networking.NetworkDto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class ProductDto extends NetworkDto {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @SerializedName("sortkey")
    public Integer identifier;

    @SerializedName("lang")
    public String languageCode;

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

    @SerializedName("last_edit_dates_tags")
    public String[] lastEditDates;

    public boolean isValid() {
        boolean hasName = name != null && name.length() > 0;
        boolean hasNutrients = nutrients.carbohydrates != null;
        return hasName && hasNutrients;
    }
}
