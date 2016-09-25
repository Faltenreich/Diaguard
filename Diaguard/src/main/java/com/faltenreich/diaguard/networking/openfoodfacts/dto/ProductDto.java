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

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("brands")
    public String brand;

    @SerializedName("nutriments")
    public NutrimentsDto nutriments;
}
