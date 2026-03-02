package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.data.export.ExportType

data class ExportFile(
    val dateTime: String,
    val type: ExportType,
)