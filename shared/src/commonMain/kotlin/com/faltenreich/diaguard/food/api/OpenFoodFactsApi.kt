package com.faltenreich.diaguard.food.api

import com.faltenreich.diaguard.shared.networking.NetworkingClient
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.Serialization

class OpenFoodFactsApi(
    private val client: NetworkingClient,
    private val serialization: Serialization,
) {

    suspend fun search(query: String?, page: Int): OpenFoodFactsResponse {
        // TODO: Get dynamically
        val countryCode = "DE"
        val languageCode = "DE"

        val arguments = "search_terms=%s&page=%d&page_size=%d&cc=%s&lc=%s&json=1"
        val url = "https://world.openfoodfacts.org/cgi/search.pl?$arguments".format(
            query ?: "",
            page + 1, // Paging of this api starts at page 1
            PAGE_SIZE,
            countryCode,
            languageCode,
            IS_JSON,
        )
        val json = client.request(url)
        val response = serialization.decodeJson<OpenFoodFactsResponse>(json)
        return response
    }

    companion object {

        private const val HOST = "https://world.openfoodfacts.org"
        private const val PAGE_SIZE = 50
        private const val IS_JSON = 1
    }
}