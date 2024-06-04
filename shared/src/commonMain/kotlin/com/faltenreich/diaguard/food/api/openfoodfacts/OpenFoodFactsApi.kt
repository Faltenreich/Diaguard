package com.faltenreich.diaguard.food.api.openfoodfacts

import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.networking.NetworkingClient
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
        val countryCode = locale.region
        val languageCode = locale.language

        // TODO: Improve performance of requests that currently take more than 5s
        // TODO: Shrink response by passing arguments
        val arguments = "search_terms=%s&page=%d&page_size=%d&cc=%s&lc=%s&json=1"
        val url = "https://world.openfoodfacts.org/cgi/search.pl?$arguments".format(
            query ?: "",
            // Pagination starts at page 1
            page.page + 1,
            page.pageSize,
            countryCode,
            languageCode,
        )
        return try {
            Logger.debug("Requesting $url")
            val json = client.request(url)
            Logger.debug("Requested $url: $json")
            val response = serialization.decodeJson<OpenFoodFactsResponse>(json)
            // Products are returned for more languages than requested
            val remote = response.products
                // Products are returned in reverse order
                .reversed()
                .filter { product ->
                    product.name?.isNotBlank() == true
                        && product.nutrients?.carbohydrates != null
                        && product.languageCode == languageCode
                }
            mapper(remote).sortedBy(FoodFromApi::name)
        } catch (exception: Exception) {
            Logger.error("Request failed for $url", exception)
            // FIXME: java.util.concurrent.CancellationException: Child of the scoped flow was cancelled
            // TODO: Propagate error to user
            emptyList()
        }
    }
}