package com.faltenreich.diaguard.measurement.value.usecase

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.entry.EntryRepository
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.data.DatabaseKey
import kotlinx.coroutines.flow.first

/**
 * Convenience use case for creating a [MeasurementValue]
 */
class StoreMeasurementValueUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val entryRepository: EntryRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    suspend operator fun invoke(
        value: Double,
        propertyKey: DatabaseKey.MeasurementProperty,
    ) {
        val now = dateTimeFactory.now()

        val entryId = entryRepository.create(
            Entry.User(
                dateTime = now,
                note = null,
            )
        )
        val entry = entryRepository.getById(entryId)!!

        val property = propertyRepository.observeByKey(propertyKey).first()!!

        valueRepository.create(
            MeasurementValue.User(
                value = value,
                property = property,
                entry = entry,
            )
        )
    }
}