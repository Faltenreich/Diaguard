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
    val settings: ExportSettings,
) {

    class Preview : PreviewParameterProvider<ExportFormState> {
        override val values = sequenceOf(
            ExportFormState(
                dateRange = today().let { today -> DateRange(today, today) },
                dateRangeLocalized = "DateRange",
                settings = ExportSettings(
                    date = ExportSettings.Date(
                        includeCalendarWeek = true,
                        includeDateOfExport = true,
                    ),
                    type = ExportSettings.Type(
                        selection = ExportType.PDF,
                        options = emptyList(),
                    ),
                    layout = ExportSettings.Layout(
                        selection = PdfLayout.TIMELINE,
                        options = emptyList(),
                        includePageNumber = true,
                        includeDaysWithoutEntries = true,
                    ),
                    content = ExportSettings.Content(
                        categories = listOf(
                            ExportSettings.Content.Category(
                                category = FakeFactory.category(),
                                isExported = true,
                                properties = listOf(
                                    ExportSettings.Content.Category.Property(
                                        property = property(),
                                        isExported = true,
                                    )
                                ),
                            ),
                        ),
                        includeNotes = true,
                        includeTags = true,
                    ),
                )
            )
        )
    }
}