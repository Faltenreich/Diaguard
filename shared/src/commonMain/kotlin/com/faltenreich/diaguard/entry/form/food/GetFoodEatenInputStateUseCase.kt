package com.faltenreich.diaguard.entry.form.food

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForEntryUseCase
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.localization.NumberFormatter
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_input_value_per_100g
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

// TODO: Prevent redundant fetch of MeasurementProperty and DecimalPlaces
class GetFoodEatenInputStateUseCase(
    private val getFoodEatenForEntry: GetFoodEatenForEntryUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
    private val valueMapper: MeasurementValueMapper,
    private val numberFormatter: NumberFormatter,
    private val localization: Localization,
) {

    operator fun invoke(
        entry: Entry.Local?,
        food: Food.Local?,
    ): Flow<List<FoodEatenInputState>> {
        return combine(
            entry?.let(getFoodEatenForEntry::invoke) ?: flowOf(emptyList()),
            propertyRepository.observeByKey(DatabaseKey.MeasurementProperty.MEAL).map(::checkNotNull),
            getPreference(DecimalPlacesPreference),
        ) { foodEatenForEntry, property, decimalPlaces ->
            val foodInputForEntry = foodEatenForEntry.map { foodEaten ->
                FoodEatenInputState(
                    food = foodEaten.food,
                    amountPer100g = getAmountPer100g(
                        carbohydrates = foodEaten.food.carbohydrates,
                        property = property,
                        decimalPlaces = decimalPlaces,
                    ),
                    amountInGrams = numberFormatter(
                        number = foodEaten.amountInGrams,
                        scale = decimalPlaces,
                    ),
                )
            }
            val foodInputForFood = food?.let {
                FoodEatenInputState(
                    food = food,
                    amountPer100g = getAmountPer100g(
                        carbohydrates = food.carbohydrates,
                        property = property,
                        decimalPlaces = decimalPlaces,
                    ),
                    amountInGrams = "",
                )
            }
            foodInputForEntry + listOfNotNull(foodInputForFood)
        }
    }

    operator fun invoke(food: Food.Local): Flow<FoodEatenInputState> {
        return combine(
            propertyRepository.observeByKey(DatabaseKey.MeasurementProperty.MEAL).map(::checkNotNull),
            getPreference(DecimalPlacesPreference),
        ) { property, decimalPlaces ->
            FoodEatenInputState(
                food = food,
                amountPer100g = getAmountPer100g(
                    carbohydrates = food.carbohydrates,
                    property = property,
                    decimalPlaces = decimalPlaces,
                ),
                amountInGrams = "",
            )
        }
    }

    private fun getAmountPer100g(
        carbohydrates: Double,
        property: MeasurementProperty.Local,
        decimalPlaces: Int,
    ): String {
        val valueLocalized = valueMapper(
            value = carbohydrates,
            property = property,
            decimalPlaces = decimalPlaces,
        ).value
        return localization.getString(
            Res.string.food_input_value_per_100g,
            valueLocalized,
            property.unit.abbreviation,
        )
    }
}