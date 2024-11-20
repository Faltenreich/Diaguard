package com.faltenreich.diaguard.backup.user.write

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class WriteBackupFormViewModel : ViewModel<WriteBackupFormState, WriteBackupFormIntent, Unit>() {

    override val state = MutableStateFlow<WriteBackupFormState>(WriteBackupFormState.Idle)

    override suspend fun handleIntent(intent: WriteBackupFormIntent) {
        when (intent) {
            is WriteBackupFormIntent.Start -> start()
            is WriteBackupFormIntent.Store -> TODO()
        }
    }

    private suspend fun start() {
        state.update { WriteBackupFormState.Loading }
        delay(1.toDuration(DurationUnit.SECONDS))
        state.update { WriteBackupFormState.Completed }
    }
}