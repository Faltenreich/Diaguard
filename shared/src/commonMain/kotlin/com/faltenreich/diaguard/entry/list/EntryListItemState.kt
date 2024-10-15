package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.MeasurementCategory

data class EntryListItemState(
    val entry: Entry.Local,
    val dateTimeLocalized: String,
    val foodEatenLocalized: List<String>,
    val values: List<Value>,
) {

    data class Value(
        val category: MeasurementCategory,
        val valueLocalized: String,
    )
}