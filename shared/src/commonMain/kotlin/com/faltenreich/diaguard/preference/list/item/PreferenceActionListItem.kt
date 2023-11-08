package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.StringResource

class PreferenceActionListItem(
    title: StringResource,
    subtitle: String?,
    // FIXME: Find way to pass Composable context
    private val action: (Navigator) -> Unit,
) : PreferenceListItem(title, subtitle) {

    @Composable
    override fun Content(modifier: Modifier) {
        val navigator = LocalNavigator.currentOrThrow
        PreferenceListItemLayout(
            preference = this,
            modifier = modifier.clickable { action(navigator) },
        )
    }
}