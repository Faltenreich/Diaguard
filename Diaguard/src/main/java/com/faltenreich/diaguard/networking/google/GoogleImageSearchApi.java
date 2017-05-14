package com.faltenreich.diaguard.networking.google;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Faltenreich on 23.09.2016.
 */

interface GoogleImageSearchApi {

    @GET("/search")
    Response search(@Query("q") String query, @Query("tbm") String tbm, @Query("tbs") String license);
}
