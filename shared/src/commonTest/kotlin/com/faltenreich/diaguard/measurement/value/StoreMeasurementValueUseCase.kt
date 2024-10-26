package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
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

        val property = propertyRepository.observeByKey(propertyKey.key).first()!!

        valueRepository.create(
            MeasurementValue.User(
                value = value,
                property = property,
                entry = entry,
            )
        )
    }
}