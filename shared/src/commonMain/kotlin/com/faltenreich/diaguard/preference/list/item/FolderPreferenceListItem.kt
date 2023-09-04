package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.preference.list.Preference

@Composable
fun FolderPreferenceListItem(
    preference: Preference.Folder,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    PreferenceListItemLayout(
        preference = preference,
        modifier = modifier.clickable {
            navigator.push(Screen.PreferenceList(preference.preferences))
        },
    )
}