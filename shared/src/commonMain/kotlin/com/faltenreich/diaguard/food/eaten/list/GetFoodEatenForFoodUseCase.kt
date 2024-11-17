package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.primitive.format
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
                FoodEaten.Localized(
                    local = food,
                    dateTime = dateTimeFormatter.formatDateTime(food.entry.dateTime),
                    amountInGrams = "%s %s".format(
                        numberFoodEaten(
                            number = food.amountInGrams,
                            scale = decimalPlaces,
                            locale = localization.getLocale(),
                        ),
                        localization.getString(Res.string.grams_abbreviation),
                    )
                )
            }
        }
    }
}