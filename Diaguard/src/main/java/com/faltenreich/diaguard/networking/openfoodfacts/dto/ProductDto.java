package com.faltenreich.diaguard.networking.openfoodfacts.dto;

import com.faltenreich.diaguard.networking.NetworkDto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public class ProductDto extends NetworkDto {

    @SerializedName("product_name")
    public String name;

    @SerializedName("brands")
    public String brand;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("nutriments")
    public NutrimentsDto nutriments;

    @SerializedName("origins")
    public String origin;
}
