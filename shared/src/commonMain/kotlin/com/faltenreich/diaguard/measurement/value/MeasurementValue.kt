package com.faltenreich.diaguard.measurement.value

import androidx.compose.ui.text.intl.Locale
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey

/**
 * Entity representing a default value of an [Entry]
 */
sealed interface MeasurementValue {

    val value: Double
    val property: MeasurementProperty.Local

    val isNotHighlighted: Boolean
        get() = property.range.isHighlighted.not()

    val isTooLow: Boolean
        get() = property.range.low?.let { value < it } ?: false

    val isTooHigh: Boolean
        get() = property.range.high?.let { value > it } ?: false

    data class Legacy(
        // Attention: May not be unique due to values once distributed into separate tables
        val id: Long,
        val createdAt: DateTime,
        val updatedAt: DateTime,
        override val value: Double,
        val propertyKey: DatabaseKey.MeasurementProperty,
        val entryId: Long,
    ) : MeasurementValue {

        override lateinit var property: MeasurementProperty.Local
        lateinit var entry: Entry.Local
    }

    data class User(
        override val value: Double,
        override val property: MeasurementProperty.Local,
        val entry: Entry.Local,
    ) : MeasurementValue

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val value: Double,
        override val property: MeasurementProperty.Local,
        val entry: Entry.Local,
    ) : MeasurementValue, DatabaseEntity

    data class Average(
        override val value: Double,
        override val property: MeasurementProperty.Local,
    ) : MeasurementValue

    data class Localized(
        /**
         * [MeasurementValue.value] to display to user,
         * converted via [MeasurementUnit.factor] of [MeasurementProperty.Local.selectedUnit],
         * formatted according to current [Locale]
         */
        val value: String,
    )
}