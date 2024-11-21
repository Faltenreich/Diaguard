package com.faltenreich.diaguard.backup.user.read

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ReadBackupFormViewModel : ViewModel<ReadBackupFormState, ReadBackupFormIntent, Unit>() {

    override val state = MutableStateFlow<ReadBackupFormState>(ReadBackupFormState.Idle)

    override suspend fun handleIntent(intent: ReadBackupFormIntent) {
        when (intent) {
            is ReadBackupFormIntent.Select -> select()
            is ReadBackupFormIntent.Start -> start()
        }
    }

    private fun select() {
        state.update { ReadBackupFormState.Ready }
    }

    private suspend fun start() {
        state.update { ReadBackupFormState.Loading }
        delay(1.toDuration(DurationUnit.SECONDS))
        state.update { ReadBackupFormState.Completed }
    }
}