package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.preference.list.Preference

@Composable
fun PlainPreferenceItem(
    preference: Preference.Plain,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    PreferenceListItem(
        preference = preference,
        modifier = modifier.clickable { preference.onClick(navigator) },
    )
}