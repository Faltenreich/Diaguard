package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.screen.PreferenceListScreen
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.StringResource

class PreferenceFolderListItem(
    title: StringResource,
    private val preferences: List<PreferenceListItem>,
    private val navigation: Navigation = inject(),
) : PreferenceListItem(title, null) {

    @Composable
    override fun Content(modifier: Modifier) {
        PreferenceListItemLayout(
            preference = this,
            modifier = modifier.clickable {
                navigation.push(PreferenceListScreen(preferences))
            },
        )
    }
}