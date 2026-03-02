package com.faltenreich.diaguard.export.history

sealed interface ExportHistoryIntent {

    data class DeleteExport(val file: ExportFile) : ExportHistoryIntent

    data class OpenExport(val file: ExportFile) : ExportHistoryIntent

    data class ShareExport(val file: ExportFile) : ExportHistoryIntent
}