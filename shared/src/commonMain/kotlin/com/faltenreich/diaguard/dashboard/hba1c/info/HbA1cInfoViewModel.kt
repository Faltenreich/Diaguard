package com.faltenreich.diaguard.dashboard.hba1c.info

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class HbA1cInfoViewModel : ViewModel<HbA1cInfoState, Unit, Unit>() {

    override val state = emptyFlow<HbA1cInfoState>()

    override suspend fun handleIntent(intent: Unit) {
        super.handleIntent(intent)
    }
}