package com.faltenreich.diaguard.export.pdf.note

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.PdfCell
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.localization.NumberFormatter
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.grams_abbreviation

internal class PdfNoteListFactory(
    private val localization: Localization,
    private val dateTimeFormatter: DateTimeFormatter,
    private val numberFormatter: NumberFormatter,
) {

    fun create(
        entries: List<Entry.Local>,
        decimalPlaces: Int,
        width: Float,
    ): PdfNoteList {
        val timeWidth = TIME_WIDTH
        val contentWidth = width - timeWidth
        return PdfNoteList(
            timeWidth = timeWidth,
            contentWidth = contentWidth,
            rows = entries.mapNotNull { entry ->
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
                    val time = dateTimeFormatter.formatTime(entry.dateTime.time)
                    PdfNoteList.Row(
                        time = PdfCell(PdfText(time, PdfPaint.label)),
                        content = PdfCell(PdfText(content, PdfPaint.label, contentWidth)),
                    )
                } else {
                    null
                }
            }
        )
    }

    private companion object {

        const val TIME_WIDTH = 100f
    }
}