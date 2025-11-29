package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class EntryListItemState(
    val entry: Entry.Local,
    val dateTimeLocalized: String,
    val foodEatenLocalized: List<String>,
    val categories: List<Category>,
) {

    data class Category(
        val category: MeasurementCategory.Local,
        val values: List<Value>,
    )

    data class Value(
        val property: MeasurementProperty.Local,
        val value: MeasurementValue.Local,
        val valueLocalized: String,
    )
}