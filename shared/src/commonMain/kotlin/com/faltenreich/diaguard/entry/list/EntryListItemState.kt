package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue

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