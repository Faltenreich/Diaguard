package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.category.MeasurementCategory

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
        val rows: List<Row>,
    ) {

        data class Row(
            val category: MeasurementCategory,
            val values: List<Value>,
        ) {

            data class Value(
                val dateTime: DateTime,
                val value: String,
            )
        }
    }
}