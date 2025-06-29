package com.faltenreich.diaguard.shared.view.preview

import androidx.compose.runtime.Composable
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.flow.flowOf

class PreviewScope(
    dateTimeFactory: DateTimeFactory,
) : DateTimeFactory by dateTimeFactory {

    fun entry() = Entry.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        dateTime = now(),
        note = "Note",
    ).apply {
        values = emptyList()
        entryTags = emptyList()
        foodEaten = emptyList()
    }

    fun category() = MeasurementCategory.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "Category",
        icon = "Icon",
        sortIndex = 0L,
        isActive = true,
        key = null,
    )

    fun property() = MeasurementProperty.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "Property",
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
        name = "Unit",
        abbreviation = "Unit",
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

    fun tag() = Tag.Local(
        id = 0L,
        createdAt = now(),
        updatedAt = now(),
        name = "Tag",
    )

    @Suppress("MagicNumber")
    fun DayOfWeek.localized() = toString()
        .take(3)
        .lowercase()
        .replaceFirstChar(Char::uppercase)

    @Composable
    fun <T : Any> List<T>.toLazyPagingItems(): LazyPagingItems<T> {
        return flowOf(PagingData.from(this)).collectAsLazyPagingItems()
    }
}