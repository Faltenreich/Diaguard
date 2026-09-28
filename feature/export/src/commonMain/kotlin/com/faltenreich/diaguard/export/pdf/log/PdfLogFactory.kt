package com.faltenreich.diaguard.export.pdf.log

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTintMapper
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.PdfCell
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.export.pdf.datetime.PdfDateFactory
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.localization.NumberFormatter
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.grams_abbreviation
import com.faltenreich.diaguard.resource.note
import com.faltenreich.diaguard.resource.tags

internal class PdfLogFactory(
    private val dateFactory: PdfDateFactory,
    private val dateTimeFormatter: DateTimeFormatter,
    private val valueMapper: MeasurementValueMapper,
    private val tintMapper: MeasurementValueTintMapper,
    private val numberFormatter: NumberFormatter,
    private val localization: Localization,
) {

    fun create(
        date: Date,
        width: Float,
        entries: List<Entry.Local>,
        categories: List<ExportSettings.Category>,
        decimalPlaces: Int,
    ): PdfDrawable {
        val properties = categories.flatMap { it.properties.map { it.property } }
        return PdfLog(
            date = dateFactory.create(date, width, withHours = false),
            entries = PdfLogEntries(
                width = width,
                entries = entries.mapNotNull { entry ->
                    val contentWidth = width - TIME_WIDTH - LABEL_WIDTH
                    val values = entry.values
                        .filter { it.property in properties }
                        .map {
                            val property = it.property
                            val value = valueMapper(it, decimalPlaces).value
                            val unit = property.unit.abbreviation
                            val text = "$value $unit".run {
                                if (property.category.isMeal) {
                                    this + "\n" + entry.foodEaten.joinToString("\n") { foodEaten ->
                                        val amount =
                                            numberFormatter(foodEaten.amountInGrams, decimalPlaces)
                                        val name = foodEaten.food.name
                                        val gramsAbbreviation = localization
                                            .getString(Res.string.grams_abbreviation)
                                        "$amount $gramsAbbreviation $name"
                                    }
                                } else {
                                    this
                                }
                            }

                            PdfLogEntries.Entry.Value(
                                label = PdfCell(PdfText(property.name, PdfPaint.label)),
                                // TODO: Tint like in PdfTable
                                content = PdfCell(PdfText(text, PdfPaint.normal, contentWidth))
                            )
                        }
                    val tags = entry.entryTags.takeIf(List<*>::isNotEmpty)?.let {
                        val label = localization.getString(Res.string.tags)
                        val content = entry.entryTags.joinToString(", ") { it.tag.name }
                        PdfLogEntries.Entry.Value(
                            label = PdfCell(PdfText(label, PdfPaint.label)),
                            content = PdfCell(PdfText(content, PdfPaint.normal, contentWidth))
                        )
                    }
                    val note = entry.note?.let { note ->
                        val label = localization.getString(Res.string.note)
                        PdfLogEntries.Entry.Value(
                            label = PdfCell(PdfText(label, PdfPaint.label)),
                            content = PdfCell(PdfText(note, PdfPaint.normal, contentWidth))
                        )
                    }
                    val items = values + listOfNotNull(tags, note)
                    if (items.isNotEmpty()) {
                        val time = dateTimeFormatter.formatTime(entry.dateTime.time)
                        PdfLogEntries.Entry(
                            time = PdfCell(PdfText(time, PdfPaint.label)),
                            values = items,
                        )
                    } else {
                        null
                    }
                },
            ),
        )
    }

    private companion object {

        const val TIME_WIDTH = 72f
        const val LABEL_WIDTH = 100f
    }
}