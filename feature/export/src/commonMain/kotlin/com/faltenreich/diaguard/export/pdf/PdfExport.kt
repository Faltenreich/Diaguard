package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.persistence.file.File

interface PdfExport {

    suspend fun export(settings: ExportSettings): File
}