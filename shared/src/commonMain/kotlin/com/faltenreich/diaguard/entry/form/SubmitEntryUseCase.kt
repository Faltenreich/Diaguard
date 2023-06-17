package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputViewState
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
        measurements: List<MeasurementPropertyInputViewState>,
    ) {
        val entryId = id ?: entryRepository.create(dateTime)
        entryRepository.update(
            id = entryId,
            dateTime = dateTime,
            note = note,
        )
        measurements.forEach { (_, values) ->
            values.map { (type, value, input) ->
                // TODO: Validate and normalize by unit
                val normalized = input.toDoubleOrNull() ?: return@map
                measurementValueRepository.update(
                    id = value?.id ?: measurementValueRepository.create(
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