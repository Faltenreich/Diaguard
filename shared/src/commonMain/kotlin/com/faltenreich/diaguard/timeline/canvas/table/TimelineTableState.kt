package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Rect
import com.faltenreich.diaguard.datetime.DateTime

data class TimelineTableState(
    val rectangle: Rect,
    val initialDateTime: DateTime,
    val categories: List<Category>,
) {

    val rowCount: Int = categories.sumOf { it.properties.size }

    data class Category(
        val icon: String?,
        val name: String,
        val properties: List<Property>,
    ) {

        data class Property(
            val name: String,
            val values: List<Value>,
        )

        data class Value(
            val dateTime: DateTime,
            val value: String,
        )
    }
}