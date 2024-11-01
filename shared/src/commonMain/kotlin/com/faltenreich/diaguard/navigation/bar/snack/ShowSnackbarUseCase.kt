package com.faltenreich.diaguard.navigation.bar.snack

import androidx.compose.material3.SnackbarDuration
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent

class ShowSnackbarUseCase(
    private val navigation: Navigation,
) {

    suspend operator fun invoke(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
    ) {
        navigation.postEvent(NavigationEvent.ShowSnackbar(message, actionLabel, withDismissAction, duration))
    }
}