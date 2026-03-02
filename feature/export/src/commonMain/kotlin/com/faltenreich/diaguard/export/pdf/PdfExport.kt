package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.export.ExportSettings

interface PdfExport {

    fun export(settings: ExportSettings)
}