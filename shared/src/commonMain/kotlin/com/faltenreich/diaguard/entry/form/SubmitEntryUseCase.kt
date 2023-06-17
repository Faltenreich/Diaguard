package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputData
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
        measurements: List<MeasurementTypeInputData>,
    ) {
        val entryId = id ?: entryRepository.create(dateTime)
        entryRepository.update(
            id = entryId,
            dateTime = dateTime,
            note = note,
        )
        val values = measurementValueRepository.getByEntryId(entryId)
        measurements.forEach { (type, input) ->
            // TODO: Validate and normalize by unit
            val value = values.firstOrNull { it.typeId == type.id }
            val normalized = input.toDoubleOrNull() ?: return@forEach
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