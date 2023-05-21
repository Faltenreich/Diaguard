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
        val entryId = id ?: entryRepository.create(dateTime)
        entryRepository.update(
            id = entryId,
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
                        entryId = entryId,
                    ),
                    value = normalized,
                )
            }
        }
    }
}