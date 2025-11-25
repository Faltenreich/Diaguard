package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.localization.NumberFormatter
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.data.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.GetPreferenceUseCase
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.grams_abbreviation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetFoodEatenForFoodUseCase(
    private val foodEatenRepository: FoodEatenRepository,
    private val dateTimeFormatter: DateTimeFormatter,
    private val numberFoodEaten: NumberFormatter,
    private val localization: Localization,
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(food: Food.Local): Flow<List<FoodEaten.Localized>> {
        return combine(
            foodEatenRepository.observeByFoodId(food.id),
            getPreference(DecimalPlacesPreference),
        ) { foodList, decimalPlaces ->
            foodList.map { food ->
                val amountInGrams = numberFoodEaten(
                    number = food.amountInGrams,
                    scale = decimalPlaces,
                )
                val gramsAbbreviation = localization.getString(Res.string.grams_abbreviation)

                FoodEaten.Localized(
                    local = food,
                    dateTime = dateTimeFormatter.formatDateTime(food.entry.dateTime),
                    amountInGrams = "$amountInGrams $gramsAbbreviation"
                )
            }
        }
    }
}