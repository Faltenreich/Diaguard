package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Rect
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValue

data class TimelineTableState(
    val rectangle: Rect,
    val categories: List<Category>,
) {

    data class Category(
        val category: MeasurementCategory.Local,
        val properties: List<Property>,
    )

    data class Property(
        val rectangle: Rect,
        val name: String,
        val values: List<Value>,
    )

    data class Value(
        val rectangle: Rect,
        val dateTime: DateTime,
        val value: String,
        val values: List<MeasurementValue.Local>,
    )
}