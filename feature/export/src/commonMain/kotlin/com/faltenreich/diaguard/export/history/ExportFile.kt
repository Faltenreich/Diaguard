package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.data.export.ExportType
import io.github.vinceglb.filekit.PlatformFile

data class ExportFile(
    val dateTime: String,
    val type: ExportType,
    val file: PlatformFile,
)