package com.faltenreich.diaguard.entry.form.food

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForEntryUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class GetFoodEatenInputStateUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val getFoodEatenForEntry: GetFoodEatenForEntryUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val numberFormatter: NumberFormatter,
    private val localization: Localization,
) {

    suspend operator fun invoke(entry: Entry.Local?): List<FoodEatenInputState> = withContext(dispatcher) {
        entry ?: return@withContext emptyList()
        val decimalPlaces = getPreference(DecimalPlacesPreference).firstOrNull() ?: DecimalPlacesPreference.default
        getFoodEatenForEntry(entry).map { foodEaten ->
            FoodEatenInputState(
                food = foodEaten.food,
                amountInGrams = numberFormatter(
                    number = foodEaten.amountInGrams,
                    scale = decimalPlaces,
                    locale = localization.getLocale(),
                ),
            )
        }
    }
}