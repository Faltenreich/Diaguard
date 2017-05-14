package com.faltenreich.diaguard.networking.google.dto;

import com.faltenreich.diaguard.networking.NetworkDto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Faltenreich on 14.05.2017
 */

public class GoogleImageDto extends NetworkDto {

    @SerializedName("ou")
    public String imageUrl;
}
