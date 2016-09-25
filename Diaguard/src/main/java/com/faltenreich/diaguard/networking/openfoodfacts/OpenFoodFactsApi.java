package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.openfoodfacts.dto.ProductResponseDto;
import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Faltenreich on 23.09.2016.
 */

public interface OpenFoodFactsApi {

    @GET("/api/v0/product/{productId}")
    ProductResponseDto getProduct(@Path("productId") String productId);

    @GET("/cgi/search.pl")
    SearchResponseDto search(@Query("search_terms") String query, @Query("countries") String countries,@Query("json") int json);
}
