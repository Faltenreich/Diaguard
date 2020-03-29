package com.faltenreich.diaguard.feature.food.networking;

import com.faltenreich.diaguard.feature.food.networking.dto.SearchResponseDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface OpenFoodFactsApi {

    @GET("cgi/search.pl")
    Call<SearchResponseDto> search(
        @Query("search_terms") String query,
        @Query("json") int isJson,
        @Query("page") int page,
        @Query("page_size") int pageSize
    );
}
