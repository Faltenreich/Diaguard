package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.export.PdfLayout
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTintMapper
import com.faltenreich.diaguard.data.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.localization.NumberFormatter
import com.faltenreich.diaguard.logging.Logger
import com.faltenreich.diaguard.persistence.file.File
import com.faltenreich.diaguard.persistence.file.FileRepository
import com.faltenreich.diaguard.persistence.pdf.PdfDocument
import com.faltenreich.diaguard.preference.GetPreferenceUseCase
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.export_empty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

internal class ExportPdfUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val localization: Localization,
    private val fileRepository: FileRepository,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
    private val valueMapper: MeasurementValueMapper,
    private val numberFormatter: NumberFormatter,
    private val tintMapper: MeasurementValueTintMapper,
    private val getPreference: GetPreferenceUseCase,
    private val createPage: CreatePdfPageUseCase,
) {

    suspend operator fun invoke(
        dateRange: DateRange,
        entries: List<Entry.Local>,
        settings: ExportSettings,
    ): File? = withContext(dispatcher) {
        try {
            val now = dateTimeFactory.now()
            val nowLocalized = dateTimeFormatter.formatDateTime(
                now,
                EXPORT_DATE_TIME_FORMAT,
            )
            val prefix = EXPORT_FILE_NAME_PREFIX
            val extension = ExportType.PDF.extension
            val fileName = "${prefix}_$nowLocalized.$extension"

            val file = fileRepository.createDocument(fileName, MIME_TYPE_PDF)
                ?: return@withContext null

            val pdfDocument = PdfDocument(file)
            var page = createPage(pdfDocument, dateRange.start, settings)
            val decimalPlaces = getPreference(DecimalPlacesPreference).first()

            DateProgression(dateRange).forEachIndexed { index, date ->
                val isNewPage = index != 0 &&
                    date == dateTimeFactory.dateAtStartOf(date, DateUnit.WEEK)
                if (isNewPage) {
                    page.finish()
                    page = createPage(pdfDocument, date, settings)
                }

                val entriesOfDate = entries.filter { it.dateTime.date == date }

                val content = when {
                    entriesOfDate.isNotEmpty() -> when (settings.pdfLayout) {
                        PdfLayout.LOG -> PdfLog(
                            date = date,
                            entries = entriesOfDate,
                            categories = settings.categories,
                            width = page.viewport.width,
                            decimalPlaces = decimalPlaces,
                            dateTimeFactory = dateTimeFactory,
                            dateTimeFormatter = dateTimeFormatter,
                            valueMapper = valueMapper,
                            tintMapper = tintMapper,
                            localization = localization,
                        )

                        PdfLayout.TABLE -> PdfTable(
                            date = date,
                            entries = entriesOfDate,
                            categories = settings.categories,
                            width = page.viewport.width,
                            decimalPlaces = decimalPlaces,
                            dateTimeFactory = dateTimeFactory,
                            dateTimeFormatter = dateTimeFormatter,
                            valueMapper = valueMapper,
                            tintMapper = tintMapper,
                        )

                        PdfLayout.TIMELINE -> PdfTimeline()
                    }

                    settings.includeDaysWithoutEntries -> PdfEmpty(
                        date = date,
                        label = localization.getString(Res.string.export_empty),
                        width = page.viewport.width,
                        dateTimeFormatter = dateTimeFormatter,
                    )

                    else -> return@forEachIndexed
                }
                val contentHeight = content.getSize().height

                if (!page.canMove(contentHeight)) {
                    page.finish()
                    page = createPage(pdfDocument, date, settings)
                }

                content.drawOn(page, page.offset)
                page.move(contentHeight)

                // TODO: Break between rows if needed
                val notes = PdfNotes(
                    entriesOfDate,
                    page.viewport.width,
                    decimalPlaces,
                    localization,
                    dateTimeFormatter,
                    numberFormatter,
                )
                val notesHeight = notes.getSize().height
                if (!page.canMove(notesHeight)) {
                    page.finish()
                    page = createPage(pdfDocument, date, settings)
                }
                notes.drawOn(page, page.offset)
                page.move(notesHeight)

                page.move(PdfSpacing.DAY_PADDING_BOTTOM.points)
            }

            page.finish()
            pdfDocument.close()

            file
        } catch (exception: Exception) {
            Logger.error("Export failed", exception)
            null
        }
    }

    companion object {

        private const val EXPORT_FILE_NAME_PREFIX = "Diaguard"
        private const val EXPORT_DATE_TIME_FORMAT = "yyyy-MM-dd_HH-mm"
        private const val MIME_TYPE_PDF = "application/pdf"
    }
}