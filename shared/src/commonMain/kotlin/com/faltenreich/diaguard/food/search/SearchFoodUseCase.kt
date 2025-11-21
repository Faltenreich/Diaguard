package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.FoodRepository
import com.faltenreich.diaguard.data.food.search.FoodSearchParams
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.view.paging.PagingPage
import com.faltenreich.diaguard.localization.NumberFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SearchFoodUseCase(
    private val repository: FoodRepository,
    private val getPreference: GetPreferenceUseCase,
    private val formatNumber: NumberFormatter,
) {

    suspend operator fun invoke(
        params: FoodSearchParams,
        page: PagingPage,
    ): Flow<List<Food.Localized>> {
        return combine(
            repository.observeByQuery(params, page),
            getPreference(DecimalPlacesPreference),
        ) { foodList, decimalPlaces ->
            foodList.map { food ->
                with(food) {
                    Food.Localized(
                        local = this,
                        carbohydrates = carbohydrates.localized(decimalPlaces),
                        energy = energy?.localized(decimalPlaces),
                        fat = fat?.localized(decimalPlaces),
                        fatSaturated = fatSaturated?.localized(decimalPlaces),
                        fiber = fiber?.localized(decimalPlaces),
                        proteins = proteins?.localized(decimalPlaces),
                        salt = salt?.localized(decimalPlaces),
                        sodium = sodium?.localized(decimalPlaces),
                        sugar = sugar?.localized(decimalPlaces),
                    )
                }
            }
        }
    }

    private fun Double.localized(decimalPlaces: Int): String {
        return formatNumber(
            number = this,
            scale = decimalPlaces,
        )
    }
}