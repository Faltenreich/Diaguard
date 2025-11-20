package com.faltenreich.diaguard.food.api.openfoodfacts

import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.view.paging.PagingPage
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.networking.NetworkingClient
import com.faltenreich.diaguard.networking.NetworkingRequest
import com.faltenreich.diaguard.serialization.Serialization

class OpenFoodFactsApi(
    private val client: NetworkingClient,
    private val localization: Localization,
    private val serialization: Serialization,
    private val mapper: OpenFoodFactsMapper,
) : FoodApi {

    override suspend fun search(query: String?, page: PagingPage): List<FoodFromApi> {
        val locale = localization.getLocale()

        val request = NetworkingRequest(
            host = "${locale.region}-${locale.language}.openfoodfacts.org",
            path = "cgi/search.pl",
            arguments = mapOf(
                "search_terms" to (query ?: ""),
                "page" to (page.page + 1).toString(),
                "page_size" to page.pageSize.toString(),
                "fields" to OpenFoodFactsProduct.Fields.joinToString(","),
                "json" to "1",
            ),
        )

        return try {
            Logger.debug("Requesting $request")
            val json = client.request(request)
            Logger.debug("Requested $request: $json")
            val response = serialization.decodeJson<OpenFoodFactsResponse>(json)
            val remote = response.products.reversed()
            mapper(remote).sortedBy(FoodFromApi::name)
        } catch (exception: Exception) {
            Logger.error("Request failed: $request $exception", exception)
            emptyList()
        }
    }
}