package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductResponseDto;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public interface OpenFoodFactsApi {

    @GET("/product/{productId}")
    ProductResponseDto getProduct(@Query("productId") String productId);
}
