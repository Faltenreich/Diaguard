package com.faltenreich.diaguard.startup

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class StartupViewModel(
    private val migrateData: MigrateDataUseCase,
) : ViewModel<Unit, StartupIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    override suspend fun handleIntent(intent: StartupIntent) {
        when (intent) {
            // FIXME: Does not repeat after failure
            is StartupIntent.MigrateData -> migrateData()
        }
    }
}