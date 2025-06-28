package com.faltenreich.diaguard.shared.view.preview

import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.MeasurementValue

class PreviewScope(
    dateTimeFactory: DateTimeFactory,
) : DateTimeFactory by dateTimeFactory {

    fun entry() = Entry.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        dateTime = now(),
        note = "note",
    )

    fun category() = MeasurementCategory.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "category",
        icon = "icon",
        sortIndex = 0L,
        isActive = true,
        key = null,
    )

    fun property() = MeasurementProperty.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "property",
        sortIndex = 0L,
        aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
        range = MeasurementValueRange(
            minimum = 0.0,
            low = 80.0,
            target = 120.0,
            high = 180.0,
            maximum = 1_000.0,
            isHighlighted = true,
        ),
        category = category(),
        unit = unit(),
        key = null,
        valueFactor = 1.0,
    )

    fun unit() = MeasurementUnit.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "unit",
        abbreviation = "u",
        key = null,
    )

    fun value() = MeasurementValue.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        value = 120.0,
        property = property(),
        entry = entry(),
    )

    @Suppress("MagicNumber")
    fun DayOfWeek.localized() = toString()
        .take(3)
        .lowercase()
        .replaceFirstChar(Char::uppercase)
}