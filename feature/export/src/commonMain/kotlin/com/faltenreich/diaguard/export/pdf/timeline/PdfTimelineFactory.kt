package com.faltenreich.diaguard.export.pdf.timeline

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTintMapper
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.export.pdf.datetime.PdfDateFactory
import com.faltenreich.diaguard.export.pdf.note.PdfNoteListFactory
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable

internal class PdfTimelineFactory(
    private val noteFactory: PdfNoteListFactory,
    private val dateFactory: PdfDateFactory,
    private val dateTimeFactory: DateTimeFactory,
    private val valueMapper: MeasurementValueMapper,
    private val tintMapper: MeasurementValueTintMapper,
) {

    fun create(
        date: Date,
        width: Float,
        entries: List<Entry.Local>,
        categories: List<ExportSettings.Category>,
        decimalPlaces: Int,
    ): PdfDrawable {
        return PdfTimeline(
            date = dateFactory.create(date, width, withHours = true),
            chart = PdfTimelineChart(
                width = width,
                values = entries.flatMap { entry ->
                    entry.values.filter { value -> value.property.category.isBloodSugar }
                }.takeIf { categories.any { it.category.isBloodSugar } }
            ),
            table = PdfTimelineTable(),
            notes = noteFactory.create(
                entries = entries,
                decimalPlaces = decimalPlaces,
                width = width,
            ),
        )
    }
}