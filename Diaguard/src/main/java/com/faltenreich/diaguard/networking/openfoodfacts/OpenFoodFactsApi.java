package com.faltenreich.diaguard.networking.openfoodfacts;

import com.faltenreich.diaguard.networking.openfoodfacts.dto.SearchResponseDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface OpenFoodFactsApi {

    @GET("/cgi/search.pl")
    Call<SearchResponseDto> search(@Query("search_terms") String query, @Query("json") int json, @Query("page_size") int pageSize, @Query("page") int page);
}
