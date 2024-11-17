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
}