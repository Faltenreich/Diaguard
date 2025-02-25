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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class GetFoodEatenInputStateUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val getFoodEatenForEntry: GetFoodEatenForEntryUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
    private val valueMapper: MeasurementValueMapper,
    private val numberFormatter: NumberFormatter,
    private val localization: Localization,
) {

    suspend operator fun invoke(entry: Entry.Local): Flow<List<FoodEatenInputState>> = withContext(dispatcher) {
        combine(
            propertyRepository.observeByKey(DatabaseKey.MeasurementProperty.MEAL),
            getPreference(DecimalPlacesPreference),
        ) { property, decimalPlaces ->
            getFoodEatenForEntry(entry).map { foodEaten ->
                FoodEatenInputState(
                    food = foodEaten.food,
                    amountPer100g = valueMapper(
                        value = foodEaten.food.carbohydrates,
                        unit = requireNotNull(property).selectedUnit,
                        decimalPlaces = decimalPlaces,
                    ).value,
                    amountInGrams = numberFormatter(
                        number = foodEaten.amountInGrams,
                        scale = decimalPlaces,
                        locale = localization.getLocale(),
                    ),
                )
            }
        }
    }

    suspend operator fun invoke(food: Food.Local): Flow<FoodEatenInputState> = withContext(dispatcher) {
        combine(
            propertyRepository.observeByKey(DatabaseKey.MeasurementProperty.MEAL),
            getPreference(DecimalPlacesPreference),
        ) { property, decimalPlaces ->
            FoodEatenInputState(
                food = food,
                amountPer100g = valueMapper(
                    value = food.carbohydrates,
                    unit = requireNotNull(property).selectedUnit,
                    decimalPlaces = decimalPlaces,
                ).value,
                amountInGrams = "",
            )
        }
    }
}