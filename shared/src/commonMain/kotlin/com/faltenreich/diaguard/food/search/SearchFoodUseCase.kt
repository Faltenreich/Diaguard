package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.localization.NumberFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchFoodUseCase(
    private val repository: FoodRepository,
    private val getPreference: GetPreferenceUseCase,
    private val formatNumber: NumberFormatter,
) {

    suspend operator fun invoke(
        params: FoodSearchParams,
        page: PagingPage,
    ): Flow<List<Food.Localized>> {
        return getPreference(DecimalPlacesPreference).map { decimalPlaces ->
            repository.getByQuery(params, page).map { food ->
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