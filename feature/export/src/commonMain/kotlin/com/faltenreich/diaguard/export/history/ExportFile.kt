package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.persistence.file.File

data class ExportFile(
    val dateTime: String,
    val type: ExportType,
    val file: File,
)