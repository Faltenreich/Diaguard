package com.faltenreich.diaguard.backup.user.read

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class ReadBackupFormViewModel : ViewModel<Unit, ReadBackupFormIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    override suspend fun handleIntent(intent: ReadBackupFormIntent) {
        when (intent) {
            is ReadBackupFormIntent.Confirm -> TODO()
        }
    }
}