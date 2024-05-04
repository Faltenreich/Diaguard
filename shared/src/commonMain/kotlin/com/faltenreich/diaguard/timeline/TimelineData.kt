package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.DateTime

data class TimelineData(
    val chart: Chart,
    val table: Table,
) {

    data class Chart(
        val values: List<Value>,
    ) {

        data class Value(
            val dateTime: DateTime,
            val value: Double,
        )
    }

    data class Table(
        val categories: List<Category>,
    ) {

        val rowCount: Int = categories.sumOf { it.properties.size }

        data class Category(
            val label: String,
            val values: List<Value>, // Aggregation if multi-property
            val properties: List<Property>, // Empty if single-property
        )

        data class Property(
            val label: String,
            val values: List<Value>,
        )

        data class Value(
            val dateTime: DateTime,
            val value: String,
        )
    }
}