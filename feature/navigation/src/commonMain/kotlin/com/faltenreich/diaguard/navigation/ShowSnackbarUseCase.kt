package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.data.navigation.NavigationEvent
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.view.bar.SnackbarDuration
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

    @Deprecated("Avoid Resources in UseCases")
    suspend operator fun invoke(
        messageResource: StringResource,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel != null) SnackbarDuration.INDEFINITE
            else SnackbarDuration.SHORT,
    ) {
        invoke(
            message = localization.getString(messageResource),
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
        )
    }
}