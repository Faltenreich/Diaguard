package com.faltenreich.diaguard.preference.list.item.screen

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
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
                subtitle = getString(startScreen.labelResource) ,
                options = StartScreen.values().map { value ->
                    SelectablePreferenceOption(
                        label = { getString(value.labelResource) },
                        isSelected = value == startScreen,
                        onSelected = { setStartScreen(value) },
                    )
                },
            )
        }
    }
}