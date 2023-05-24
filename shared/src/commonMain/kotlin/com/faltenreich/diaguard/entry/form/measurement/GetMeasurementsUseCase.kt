package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetMeasurementsUseCase(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(entryId: Long?): Flow<MeasurementInputViewState> {
        return measurementPropertyRepository.getAll().map { properties ->
            val values = entryId?.let(measurementValueRepository::getByEntryId)?.firstOrNull()
            MeasurementInputViewState(properties = properties.map { property ->
                // FIXME: Replace with lateinit var when available
                val types = measurementTypeRepository.getByPropertyId(property.id).first()
                MeasurementInputViewState.Property(
                    property = property,
                    values = types.map { type ->
                        val value = values?.firstOrNull { it.typeId == type.id }
                        MeasurementInputViewState.Property.Value(
                            type = type,
                            valueId = value?.id,
                            input = value?.value?.toString() ?: "",
                        )
                    }
                )
            })
        }.flowOn(dispatcher)
    }
}