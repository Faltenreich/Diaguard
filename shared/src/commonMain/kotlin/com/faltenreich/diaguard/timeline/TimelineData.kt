package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

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
            val category: MeasurementCategory,
            val properties: List<Property>,
        ) {

            data class Property(
                val property: MeasurementProperty,
                val values: List<Value>,
            )

            data class Value(
                val dateTime: DateTime,
                val value: String,
            )
        }
    }
}