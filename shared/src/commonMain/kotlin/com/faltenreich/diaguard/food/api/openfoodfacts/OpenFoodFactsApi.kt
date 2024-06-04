package com.faltenreich.diaguard.food.api.openfoodfacts

import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.networking.NetworkingClient
import com.faltenreich.diaguard.shared.networking.NetworkingRequest
import com.faltenreich.diaguard.shared.serialization.Serialization

class OpenFoodFactsApi(
    private val client: NetworkingClient = inject(),
    private val localization: Localization = inject(),
    private val serialization: Serialization = inject(),
    private val mapper: OpenFoodFactsMapper = inject(),
) : FoodApi {

    override suspend fun search(query: String?, page: PagingPage): List<FoodFromApi> {
        val locale = localization.getLocale()

        val fields = listOf(
            "sortkey",
            "lang",
            "product_name",
            "brands",
            "ingredients_text",
            "labels",
            "nutriments",
            "last_edit_dates_tags",
            "carbohydrates_100g",
            "energy_100g",
            "fat_100g",
            "saturated-fat_100g",
            "fiber_100g",
            "proteins_100g",
            "salt_100g",
            "sodium_100g",
            "sugars_100g",
        ).joinToString(",")

        val arguments = mapOf(
            "search_terms" to (query ?: ""),
            "page" to (page.page + 1),
            "page_size" to page.pageSize,
            "cc" to locale.region,
            "lc" to locale.language,
            "json" to "1",
            "fields" to fields,
        )

        val request = NetworkingRequest(
            host = HOST,
            path = "search",
            arguments = arguments,
        )
        val url = request.url()

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
                        && product.carbohydrates != null
                        && product.languageCode == locale.language
                }
            mapper(remote).sortedBy(FoodFromApi::name)
        } catch (exception: Exception) {
            Logger.error("Request failed for $url", exception)
            // FIXME: java.util.concurrent.CancellationException: Child of the scoped flow was cancelled
            // TODO: Propagate error to user
            emptyList()
        }
    }

    companion object {

        private const val HOST = "https://world.openfoodfacts.org/api/v2"
    }
}