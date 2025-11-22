package com.faltenreich.diaguard.data.food.api.openfoodfacts

import com.faltenreich.diaguard.data.food.api.FoodApi
import com.faltenreich.diaguard.data.food.api.FoodFromApi
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.network.NetworkClient
import com.faltenreich.diaguard.network.NetworkRequest
import com.faltenreich.diaguard.serialization.Serialization
import com.faltenreich.diaguard.view.paging.PagingPage

internal class OpenFoodFactsApi(
    private val client: NetworkClient,
    private val localization: Localization,
    private val serialization: Serialization,
    private val mapper: OpenFoodFactsMapper,
) : FoodApi {

    override suspend fun search(query: String?, page: PagingPage): List<FoodFromApi> {
        val locale = localization.getLocale()

        val request = NetworkRequest(
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