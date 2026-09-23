package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.localization.NumberFormatter
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.grams_abbreviation

internal class PdfNotes(
    entries: List<Entry.Local>,
    private val width: Float,
    decimalPlaces: Int,
    localization: Localization,
    dateTimeFormatter: DateTimeFormatter,
    numberFormatter: NumberFormatter,
) : PdfDrawable {

    data class Row(
        val dateTime: PdfDrawable,
        val content: PdfDrawable,
    )

    private val rows: List<Row> = entries.mapNotNull { entry ->
        val gramsAbbreviation = localization.getString(Res.string.grams_abbreviation)
        val notesAndTags = (listOfNotNull(entry.note) + entry.entryTags.map { it.tag.name })
            .joinToString(", ")
            .takeIf(String::isNotBlank)
        val foodEaten = entry.foodEaten
            .joinToString("\n") { foodEaten ->
                val amount = numberFormatter(foodEaten.amountInGrams, decimalPlaces)
                val name = foodEaten.food.name
                "$amount $gramsAbbreviation $name"
            }
            .takeIf(String::isNotBlank)
        val content = listOfNotNull(notesAndTags, foodEaten)
            .joinToString("\n")
        if (content.isNotEmpty()) {
            val dateTime = dateTimeFormatter.formatTime(entry.dateTime.time)
            val maxWidth = width - DAY_WIDTH
            Row(
                dateTime = PdfCell(PdfText(dateTime, PdfPaint.label)),
                content = PdfCell(PdfText(content, PdfPaint.label, maxWidth)),
            )
        } else {
            null
        }
    }
    private val divider = PdfBackground(PdfSize(width = width, height = .75f), PdfPaint.divider)

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = rows.sumOf { it.content.getSize().height.toDouble() }.toFloat(),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        if (rows.isNotEmpty()) {
            var position = position
            rows.forEach { row ->
                divider.drawOn(page, position)

                row.dateTime.drawOn(page, position)
                row.content.drawOn(page, position.copy(x = position.x + DAY_WIDTH))
                position = position.copy(y = position.y + row.content.getSize().height)
            }
        }
    }

    private companion object {

        const val DAY_WIDTH = 100f
    }
}