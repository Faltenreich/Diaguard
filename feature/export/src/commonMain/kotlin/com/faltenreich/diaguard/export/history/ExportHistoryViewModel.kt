package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.persistence.file.OpenFileUseCase
import kotlinx.coroutines.flow.map

internal class ExportHistoryViewModel(
    getFiles: GetExportFilesUseCase,
    private val openFile: OpenFileUseCase,
) : ViewModel<ExportHistoryState, ExportHistoryIntent, Unit>() {

    override val state = getFiles().map(::ExportHistoryState)

    override suspend fun handleIntent(intent: ExportHistoryIntent) {
        when (intent) {
            is ExportHistoryIntent.DeleteExport -> TODO()
            is ExportHistoryIntent.ShareExport -> TODO()
            is ExportHistoryIntent.OpenExport -> openFile(intent.file.file)
        }
    }
}