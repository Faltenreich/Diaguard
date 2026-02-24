package com.faltenreich.diaguard.export.form

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.export.PdfLayout
import com.faltenreich.diaguard.data.fake.FakeFactory
import com.faltenreich.diaguard.data.fake.FakeFactory.property
import com.faltenreich.diaguard.data.fake.FakeFactory.today
import com.faltenreich.diaguard.datetime.DateRange

internal data class ExportFormState(
    val dateRange: DateRange,
    val dateRangeLocalized: String,
    val exportTypes: List<ExportType>,
    val pdfLayouts: List<PdfLayout>,
    val settings: ExportSettings,
) {

    class Preview : PreviewParameterProvider<ExportFormState> {
        override val values = sequenceOf(
            ExportFormState(
                dateRange = today().let { today -> DateRange(today, today) },
                dateRangeLocalized = "DateRange",
                exportTypes = ExportType.entries,
                pdfLayouts = PdfLayout.entries,
                settings = ExportSettings(
                    categories = listOf(
                        ExportSettings.Category(
                            category = FakeFactory.category(),
                            isExported = true,
                            properties = listOf(
                                ExportSettings.Category.Property(
                                    property = property(),
                                    isExported = true,
                                )
                            ),
                        ),
                    ),
                    exportType = ExportType.PDF,
                    includeCalendarWeek = true,
                    includeDateOfExport = true,
                    includeDaysWithoutEntries = true,
                    includePageNumber = true,
                    includeNotes = true,
                    includeTags = true,
                    pdfLayout = PdfLayout.TIMELINE,
                )
            )
        )
    }
}