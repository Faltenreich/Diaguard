package com.faltenreich.diaguard.preference.list.usecase

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.item.SelectablePreferenceOption
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetColorSchemePreferenceUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    operator fun invoke(): Flow<Preference> {
        return preferenceStore.colorScheme.map { colorScheme ->
            Preference.Selection(
                title = MR.strings.start_screen,
                subtitle = getString(colorScheme.labelResource) ,
                options = ColorScheme.values().map { value ->
                    SelectablePreferenceOption(
                        label = { getString(value.labelResource) },
                        isSelected = value == colorScheme,
                        // TODO: Apply theme
                        onSelected = { preferenceStore.setColorScheme(value) },
                    )
                },
            )
        }
    }
}