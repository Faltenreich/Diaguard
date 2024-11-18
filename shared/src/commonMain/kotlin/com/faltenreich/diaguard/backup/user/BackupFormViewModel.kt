package com.faltenreich.diaguard.backup.user

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class BackupFormViewModel : ViewModel<Unit, BackupFormIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    override suspend fun handleIntent(intent: BackupFormIntent) {
        when (intent) {
            is BackupFormIntent.Read -> TODO()
            is BackupFormIntent.Write -> TODO()
        }
    }
}