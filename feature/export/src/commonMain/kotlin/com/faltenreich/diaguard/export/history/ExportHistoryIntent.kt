package com.faltenreich.diaguard.export.history

sealed interface ExportHistoryIntent {

    data class OpenExport(val file: ExportFile) : ExportHistoryIntent
}