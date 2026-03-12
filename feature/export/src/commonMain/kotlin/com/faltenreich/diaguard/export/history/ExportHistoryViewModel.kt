package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.persistence.file.DeleteFileUseCase
import com.faltenreich.diaguard.persistence.file.OpenFileUseCase
import com.faltenreich.diaguard.persistence.file.ShareFileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class ExportHistoryViewModel(
    private val getFiles: GetExportFilesUseCase,
    private val openFile: OpenFileUseCase,
    private val deleteFile: DeleteFileUseCase,
    private val shareFile: ShareFileUseCase,
) : ViewModel<ExportHistoryState, ExportHistoryIntent, Unit>() {

    private val files = MutableStateFlow<List<ExportFile>>(emptyList())
    override val state = files.map(::ExportHistoryState)

    init {
        refreshFiles()
    }

    override suspend fun handleIntent(intent: ExportHistoryIntent) = with(intent) {
        when (this) {
            is ExportHistoryIntent.DeleteExport -> {
                deleteFile(exportFile.file)
                refreshFiles()
            }

            is ExportHistoryIntent.ShareExport -> shareFile(exportFile.file)
            is ExportHistoryIntent.OpenExport -> openFile(exportFile.file)
        }
    }

    private fun refreshFiles() {
        files.update { getFiles() }
    }
}