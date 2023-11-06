package com.faltenreich.diaguard.preference.list.item.color

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetColorSchemePreferenceUseCase(
    private val getColorScheme: GetColorSchemeUseCase = inject(),
    private val setColorScheme: SetColorSchemeUseCase = inject(),
) {

    operator fun invoke(): Flow<Preference> {
        return getColorScheme().map { colorScheme ->
            Preference.Selection(
                title = MR.strings.start_screen,
                subtitle = getString(colorScheme.labelResource) ,
                options = ColorScheme.values().map { value ->
                    SelectablePreferenceOption(
                        label = { getString(value.labelResource) },
                        isSelected = value == colorScheme,
                        onSelected = { setColorScheme(value) },
                    )
                },
            )
        }
    }
}