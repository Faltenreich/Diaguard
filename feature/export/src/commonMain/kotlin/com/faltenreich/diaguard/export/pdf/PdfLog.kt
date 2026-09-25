package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings.Category
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTintMapper
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.datetime.PdfDate
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfRectangle
import com.faltenreich.diaguard.persistence.pdf.PdfSize
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.note
import com.faltenreich.diaguard.resource.tags

internal class PdfLog(
    date: Date,
    entries: List<Entry.Local>,
    categories: List<Category>,
    private val width: Float,
    private val decimalPlaces: Int,
    private val dateTimeFactory: DateTimeFactory,
    dateTimeFormatter: DateTimeFormatter,
    private val valueMapper: MeasurementValueMapper,
    private val tintMapper: MeasurementValueTintMapper,
    private val localization: Localization,
) : PdfDrawable {

    data class Row(
        val time: PdfDrawable,
        val items: List<Item>,
    ) {

        data class Item(
            val label: PdfDrawable,
            val content: PdfDrawable,
        )
    }

    private val date = PdfDate(date, dateTimeFormatter)
    private val properties = categories.flatMap { it.properties.map { it.property } }
    private val rows: List<Row> = entries.mapNotNull { entry ->
        val contentWidth = width - TIME_WIDTH - LABEL_WIDTH
        val values = entry.values
            .filter { it.property in properties }
            .map { value ->
                val text = valueMapper(value, decimalPlaces).value
                Row.Item(
                    label = PdfCell(PdfText(value.property.name, PdfPaint.label)),
                    // TODO: Add notes and tags and remove from parent
                    // TODO: Tint like in PdfTable
                    content = PdfCell(PdfText(text, PdfPaint.normal, contentWidth))
                )
            }
        val tags = entry.entryTags.takeIf(List<*>::isNotEmpty)?.let {
            val label = localization.getString(Res.string.tags)
            val content = entry.entryTags.joinToString(", ") { it.tag.name }
            Row.Item(
                label = PdfCell(PdfText(label, PdfPaint.label)),
                content = PdfCell(PdfText(content, PdfPaint.normal, contentWidth))
            )
        }
        val note = entry.note?.let { note ->
            val label = localization.getString(Res.string.note)
            Row.Item(
                label = PdfCell(PdfText(label, PdfPaint.label)),
                content = PdfCell(PdfText(note, PdfPaint.normal, contentWidth))
            )
        }
        val items = values + listOfNotNull(tags, note)
        if (items.isNotEmpty()) {
            val time = dateTimeFormatter.formatTime(entry.dateTime.time)
            Row(
                time = PdfCell(PdfText(time, PdfPaint.label)),
                items = items,
            )
        } else {
            null
        }
    }

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = date.getSize().height +
                rows.sumOf { it.items.sumOf { it.content.getSize().height.toDouble() } }.toFloat(),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)

        var position = position.copy(y = position.y + date.getSize().height)
        rows.forEachIndexed { index, row ->
            if (index % 2 == 0) {
                val height = row.items.sumOf { it.content.getSize().height.toDouble() }.toFloat()
                val rectangle = PdfRectangle(position, PdfSize(width, height))
                val background = PdfBackground(rectangle.size, PdfPaint.background)
                background.drawOn(page, rectangle.position)
            }

            row.time.drawOn(page, position)

            row.items.forEach { item ->
                item.label.drawOn(page, position.copy(x = position.x + TIME_WIDTH))
                item.content.drawOn(
                    page,
                    position.copy(x = position.x + TIME_WIDTH + LABEL_WIDTH)
                )
                position = position.copy(y = position.y + item.content.getSize().height)
            }
        }
    }

    private companion object {

        const val TIME_WIDTH = 72f
        const val LABEL_WIDTH = 100f
    }
}