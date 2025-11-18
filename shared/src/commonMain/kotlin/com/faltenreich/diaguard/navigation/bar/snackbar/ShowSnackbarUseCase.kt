package com.faltenreich.diaguard.navigation.bar.snackbar

import androidx.compose.material3.SnackbarDuration
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.localization.Localization
import org.jetbrains.compose.resources.StringResource

class ShowSnackbarUseCase(
    private val navigation: Navigation,
    private val localization: Localization,
) {

    suspend operator fun invoke(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
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

    suspend operator fun invoke(
        messageResource: StringResource,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
    ) {
        invoke(
            message = localization.getString(messageResource),
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
        )
    }
}