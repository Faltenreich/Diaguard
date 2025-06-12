package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Rect
import com.faltenreich.diaguard.datetime.DateTime

data class TimelineTableState(
    val rectangle: Rect,
    val categories: List<Category>,
) {

    data class Category(
        val properties: List<Property>,
    ) {

        data class Property(
            val rectangle: Rect,
            val icon: String,
            val name: String,
            val values: List<Value>,
        )

        data class Value(
            val rectangle: Rect,
            val value: String,
            val dateTime: DateTime,
        )
    }
}