package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.Preference

@Composable
fun PlainPreferenceItem(
    preference: Preference.Plain,
    modifier: Modifier = Modifier,
) {
    PreferenceListItem(
        preference = preference,
        modifier = modifier.clickable {},
    )
}