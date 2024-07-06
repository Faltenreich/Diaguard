package com.faltenreich.diaguard.dashboard.latest

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class GetLatestBloodSugarUseCase(
    private val categoryRepository: MeasurementCategoryRepository,
    private val valueRepository: MeasurementValueRepository,
    private val valueMapper: MeasurementValueMapper,
    private val getValueColor: GetMeasurementValueTintUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<DashboardViewState.LatestBloodSugar?> {
        return categoryRepository.observeBloodSugar().flatMapLatest { category ->
            val dateTime = dateTimeFactory.now()
            val categoryId = category?.id ?: return@flatMapLatest flowOf(null)
            combine(
                valueRepository.observeLatestByCategoryId(
                    dateTime = dateTime,
                    categoryId = categoryId,
                ),
                getPreference(DecimalPlaces),
            ) { value, decimalPlaces ->
                when (value) {
                    null -> null
                    else -> DashboardViewState.LatestBloodSugar(
                        entry = value.entry,
                        value = valueMapper(
                            value = value,
                            decimalPlaces = decimalPlaces,
                        ),
                        tint = getValueColor(value),
                        timePassed = dateTimeFormatter.formatTimePassed(
                            start = value.entry.dateTime,
                            end = dateTimeFactory.now(),
                        ),
                    )
                }
            }
        }
    }
}