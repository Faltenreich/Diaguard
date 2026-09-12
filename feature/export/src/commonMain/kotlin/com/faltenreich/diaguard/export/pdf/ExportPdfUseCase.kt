package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.persistence.file.File

interface ExportPdfUseCase {

    suspend operator fun invoke(
        dateRange: DateRange,
        entries: List<Entry.Local>,
        settings: ExportSettings,
    ): File?
}