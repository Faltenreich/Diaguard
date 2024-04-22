package com.faltenreich.diaguard.food.api.openfoodfacts

import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.networking.NetworkingClient
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OpenFoodFactsApi(
    private val client: NetworkingClient,
    private val localization: Localization,
    private val serialization: Serialization,
    private val mapper: OpenFoodFactsMapper,
) : FoodApi {

    override fun search(query: String?, page: Int): Flow<List<FoodFromApi>> = flow {
        val locale = localization.getLocale()
        val countryCode = locale.region
        val languageCode = locale.language

        val arguments = "search_terms=%s&page=%d&page_size=%d&cc=%s&lc=%s&json=1"
        val url = "https://world.openfoodfacts.org/cgi/search.pl?$arguments".format(
            query ?: "",
            // Pagination starts at page 1
            page + 1,
            PAGE_SIZE,
            countryCode,
            languageCode,
            IS_JSON,
        )
        try {
            val json = client.request(url)
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
            val food = mapper(remote).sortedBy(FoodFromApi::name)
            emit(food)
        } catch (exception: Exception) {
            Logger.error("Failed to request food from api", exception)
            // FIXME: java.util.concurrent.CancellationException: Child of the scoped flow was cancelled
            emit(emptyList())
        }
    }

    companion object {

        private const val PAGE_SIZE = 50
        private const val IS_JSON = 1
    }
}