package com.faltenreich.diaguard.export.history

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import kotlinx.coroutines.flow.flowOf

class ExportHistoryViewModel : ViewModel<ExportHistoryState, ExportHistoryIntent, Unit>() {

    override val state = flowOf(ExportHistoryState(files = emptyList()))
}