package com.faltenreich.diaguard.food.api.openfoodfacts

import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.networking.NetworkingClient
import com.faltenreich.diaguard.shared.networking.NetworkingRequest
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.Serialization

class OpenFoodFactsApi(
    private val client: NetworkingClient = inject(),
    private val localization: Localization = inject(),
    private val serialization: Serialization = inject(),
    private val mapper: OpenFoodFactsMapper = inject(),
) : FoodApi {

    override suspend fun search(query: String?, page: PagingPage): List<FoodFromApi> {
        val locale = localization.getLocale()

        val request = NetworkingRequest(
            host = "%s-%s.openfoodfacts.org".format(locale.region, locale.language),
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