package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.navigation.Screen
import dev.icerock.moko.resources.StringResource

class PreferenceFolderListItem(
    title: StringResource,
    private val preferences: List<PreferenceListItem>,
) : PreferenceListItem(title, null) {

    @Composable
    override fun Content(modifier: Modifier) {
        val navigator = LocalNavigator.currentOrThrow
        PreferenceListItemLayout(
            preference = this,
            modifier = modifier.clickable {
                navigator.push(Screen.PreferenceList(preferences))
            },
        )
    }
}