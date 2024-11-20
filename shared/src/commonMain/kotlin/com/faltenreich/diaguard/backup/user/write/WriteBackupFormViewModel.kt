package com.faltenreich.diaguard.backup.user.write

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class WriteBackupFormViewModel : ViewModel<Unit, WriteBackupFormIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    override suspend fun handleIntent(intent: WriteBackupFormIntent) {
        when (intent) {
            is WriteBackupFormIntent.Start -> TODO()
            is WriteBackupFormIntent.Store -> TODO()
        }
    }
}