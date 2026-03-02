package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import kotlinx.coroutines.flow.map

internal class ExportHistoryViewModel(
    getFiles: GetExportFilesUseCase,
) : ViewModel<ExportHistoryState, ExportHistoryIntent, Unit>() {

    override val state = getFiles().map(::ExportHistoryState)
}