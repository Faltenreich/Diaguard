package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import kotlinx.coroutines.flow.map

internal class ExportHistoryViewModel(
    getFiles: GetExportFilesUseCase,
) : ViewModel<ExportHistoryState, ExportHistoryIntent, Unit>() {

    override val state = getFiles().map(::ExportHistoryState)

    override suspend fun handleIntent(intent: ExportHistoryIntent) {
        when (intent) {
            is ExportHistoryIntent.DeleteExport -> TODO()
            is ExportHistoryIntent.ShareExport -> TODO()
            is ExportHistoryIntent.OpenExport -> TODO()
        }
    }
}