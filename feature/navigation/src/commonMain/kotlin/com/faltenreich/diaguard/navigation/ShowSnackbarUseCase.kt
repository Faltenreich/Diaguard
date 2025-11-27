package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.data.navigation.NavigationEvent
import com.faltenreich.diaguard.view.bar.SnackbarDuration

class ShowSnackbarUseCase(private val navigation: Navigation) {

    suspend operator fun invoke(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel != null) SnackbarDuration.INDEFINITE
            else SnackbarDuration.SHORT,
    ) {
        navigation.postEvent(
            NavigationEvent.ShowSnackbar(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration,
            )
        )
    }
}