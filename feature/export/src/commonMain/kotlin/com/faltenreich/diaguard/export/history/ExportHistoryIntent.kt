package com.faltenreich.diaguard.export.history

sealed interface ExportHistoryIntent {

    data class DeleteExport(val exportFile: ExportFile) : ExportHistoryIntent

    data class OpenExport(val exportFile: ExportFile) : ExportHistoryIntent

    data class ShareExport(val exportFile: ExportFile) : ExportHistoryIntent
}