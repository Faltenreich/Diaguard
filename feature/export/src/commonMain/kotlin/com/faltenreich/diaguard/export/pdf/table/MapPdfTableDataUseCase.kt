package com.faltenreich.diaguard.export.pdf.table

import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTint
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTintMapper
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.TimeUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.export.pdf.PdfCell
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.export.pdf.note.MapPdfNoteListDataUseCase
import com.faltenreich.diaguard.persistence.pdf.PdfPaint

internal class MapPdfTableDataUseCase(
    private val mapNotes: MapPdfNoteListDataUseCase,
    private val dateTimeFactory: DateTimeFactory,
    private val valueMapper: MeasurementValueMapper,
    private val tintMapper: MeasurementValueTintMapper,
) {

    operator fun invoke(
        date: Date,
        width: Float,
        entries: List<Entry.Local>,
        categories: List<ExportSettings.Category>,
        decimalPlaces: Int,
    ): PdfTableData {
        return PdfTableData(
            date = date,
            width = width,
            categories = categories
                .filter { it.isExported }
                .map { category ->
                    PdfTableData.Category(
                        properties = category.properties
                            .filter { it.isExported }
                            .map { (property, _) ->
                                val categoryName = property.category.name
                                val labelText = listOfNotNull(
                                    categoryName,
                                    property.name.takeIf { it != categoryName },
                                ).joinToString(" ")
                                PdfTableData.Category.Property(
                                    property = PdfCell(PdfText(labelText, PdfPaint.label)),
                                    values = HOURS.map { hour ->
                                        val values = entries.flatMap { entry ->
                                            val entryTime = entry.dateTime.time
                                            val startTime =
                                                dateTimeFactory.time(
                                                    hourOfDay = hour,
                                                    minuteOfHour = 0
                                                )
                                            val endTime = dateTimeFactory.timeAtEndOf(
                                                time = startTime.copy(hourOfDay = hour + DAY_STEP - 1),
                                                unit = TimeUnit.HOUR,
                                            )
                                            if (entryTime in startTime..<endTime) {
                                                entry.values.filter { value -> value.property == property }
                                            } else {
                                                emptyList()
                                            }
                                        }
                                        val value = if (values.isNotEmpty()) {
                                            val sum = values.sumOf { it.value }
                                            val value = MeasurementValue.Average(
                                                value = when (property.aggregationStyle) {
                                                    MeasurementAggregationStyle.CUMULATIVE -> sum
                                                    MeasurementAggregationStyle.AVERAGE -> sum / values.size
                                                },
                                                property = property,
                                            )
                                            val valueLocalized =
                                                valueMapper(value, decimalPlaces).value
                                            // TODO: Check setting and get colors from Theme
                                            val color = when (tintMapper(value)) {
                                                MeasurementValueTint.NONE -> Color.Black
                                                MeasurementValueTint.LOW -> Color.Blue
                                                MeasurementValueTint.NORMAL -> Color.Black
                                                MeasurementValueTint.HIGH -> Color.Red
                                            }
                                            PdfText(valueLocalized, PdfPaint(color))
                                        } else {
                                            null
                                        }
                                        PdfTableData.Category.Property.Value(
                                            hour = hour,
                                            value = value,
                                        )
                                    }
                                )
                            }
                    )
                },
            notes = mapNotes(
                entries = entries,
                decimalPlaces = decimalPlaces,
                width = width,
            ),
        )
    }

    private companion object {

        const val DAY_HOURS = 24
        const val DAY_STEP = 2
        val HOURS = 0..<DAY_HOURS step DAY_STEP
    }
}