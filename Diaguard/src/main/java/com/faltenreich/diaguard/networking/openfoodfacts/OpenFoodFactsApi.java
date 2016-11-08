package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Faltenreich on 23.09.2016.
 */

interface OpenFoodFactsApi {

    @GET("/cgi/search.pl")
    SearchResponseDto search(@Query("search_terms") String query, @Query("json") int json, @Query("page_size") int pageSize, @Query("page") int page);
}
