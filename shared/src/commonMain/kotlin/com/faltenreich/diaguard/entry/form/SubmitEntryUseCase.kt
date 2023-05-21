package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.measurement.MeasurementInputViewState
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject

class SubmitEntryUseCase(
    private val entryRepository: EntryRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(
        id: Long?,
        dateTime: DateTime,
        note: String?,
        measurements: MeasurementInputViewState,
    ) {
        entryRepository.update(
            id = id ?: entryRepository.create(dateTime),
            dateTime = dateTime,
            note = note,
        )
        measurements.properties.forEach { (_, values) ->
            values.map { (value, type) ->
                val normalized = value.toDoubleOrNull() ?: return@map // TODO: Normalize unit
                measurementValueRepository.update(
                    id = measurementValueRepository.create(
                        value = normalized,
                        typeId = type.id,
                        measurementId = 0L, // TODO: Remove
                    ),
                    value = normalized,
                )
            }
        }
    }
}