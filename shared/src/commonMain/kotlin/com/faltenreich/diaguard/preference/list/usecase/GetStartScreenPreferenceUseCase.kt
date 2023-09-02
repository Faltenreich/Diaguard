package com.faltenreich.diaguard.preference.list.usecase

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStartScreenPreferenceUseCase(
    private val getStartScreen: GetStartScreenUseCase = inject(),
    private val setStartScreen: SetStartScreenUseCase = inject(),
) {

    operator fun invoke(): Flow<Preference> {
        return getStartScreen().map { startScreen ->
            Preference.Selection(
                title = MR.strings.start_screen,
                // TODO: subtitle = { stringResource(startScreen.labelResource) },
                subtitle = null,
                options = StartScreen.values().map { value ->
                    SelectablePreferenceOption(
                        label = { stringResource(value.labelResource) },
                        isSelected = value == startScreen,
                        onSelected = { setStartScreen(value) },
                    )
                },
            )
        }
    }
}