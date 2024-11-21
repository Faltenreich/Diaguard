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
            is ReadBackupFormIntent.Read -> read()
            is ReadBackupFormIntent.Check -> check()
            is ReadBackupFormIntent.Store -> store()
        }
    }

    private fun select() {
        state.update { ReadBackupFormState.Selected }
    }

    private suspend fun read() {
        state.update { ReadBackupFormState.Reading }
        delay(1.toDuration(DurationUnit.SECONDS))
        state.update { ReadBackupFormState.Ready }
    }

    private fun check() {
        state.update { ReadBackupFormState.Checked }
    }

    private suspend fun store() {
        state.update { ReadBackupFormState.Storing }
        delay(1.toDuration(DurationUnit.SECONDS))
        state.update { ReadBackupFormState.Completed }
    }
}