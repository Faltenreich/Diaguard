package com.faltenreich.diaguard.entry.form.food

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForEntryUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.primitive.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_input_value_per_100g
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetFoodEatenInputStateUseCase(
    private val getFoodEatenForEntry: GetFoodEatenForEntryUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
    private val valueMapper: MeasurementValueMapper,
    private val numberFormatter: NumberFormatter,
    private val localization: Localization,
) {

    operator fun invoke(entry: Entry.Local): Flow<List<FoodEatenInputState>> {
        // TODO: Reuse getAmountPer100g
        return combine(
            propertyRepository.observeByKey(DatabaseKey.MeasurementProperty.MEAL),
            getPreference(DecimalPlacesPreference),
        ) { property, decimalPlaces ->
            getFoodEatenForEntry(entry).map { foodEaten ->
                val amountPer100g = valueMapper(
                    value = foodEaten.food.carbohydrates,
                    unit = requireNotNull(property).selectedUnit,
                    decimalPlaces = decimalPlaces,
                ).value
                FoodEatenInputState(
                    food = foodEaten.food,
                    amountPer100g = localization.getString(Res.string.food_input_value_per_100g)
                        .format(amountPer100g, property.selectedUnit.abbreviation),
                    amountInGrams = numberFormatter(
                        number = foodEaten.amountInGrams,
                        scale = decimalPlaces,
                        locale = localization.getLocale(),
                    ),
                )
            }
        }
    }

    operator fun invoke(food: Food.Local): Flow<FoodEatenInputState> {
        return getAmountPer100g(food.carbohydrates).map { amountPer100g ->
            FoodEatenInputState(
                food = food,
                amountPer100g = amountPer100g,
                amountInGrams = "",
            )
        }
    }

    private fun getAmountPer100g(carbohydrates: Double): Flow<String> {
        return combine(
            propertyRepository.observeByKey(DatabaseKey.MeasurementProperty.MEAL),
            getPreference(DecimalPlacesPreference),
        ) { property, decimalPlaces ->
            val valueLocalized = valueMapper(
                value = carbohydrates,
                unit = requireNotNull(property).selectedUnit,
                decimalPlaces = decimalPlaces,
            ).value
            localization.getString(Res.string.food_input_value_per_100g)
                .format(valueLocalized, property.selectedUnit.abbreviation)
        }
    }
}