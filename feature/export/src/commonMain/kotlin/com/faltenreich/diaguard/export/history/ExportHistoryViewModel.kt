package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.persistence.file.DeleteFileUseCase
import com.faltenreich.diaguard.persistence.file.OpenFileUseCase
import kotlinx.coroutines.flow.map

internal class ExportHistoryViewModel(
    getFiles: GetExportFilesUseCase,
    private val openFile: OpenFileUseCase,
    private val deleteFile: DeleteFileUseCase,
) : ViewModel<ExportHistoryState, ExportHistoryIntent, Unit>() {

    override val state = getFiles().map(::ExportHistoryState)

    override suspend fun handleIntent(intent: ExportHistoryIntent) = with(intent) {
        when (this) {
            is ExportHistoryIntent.DeleteExport -> deleteFile(exportFile.file)
            is ExportHistoryIntent.ShareExport -> TODO()
            is ExportHistoryIntent.OpenExport -> openFile(exportFile.file)
        }
    }
}