package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.Preference
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun PlainPreferenceItem(
    preference: Preference.Plain,
    modifier: Modifier = Modifier,
) {
    PreferenceListItem(modifier = modifier.clickable {}) {
        Column {
            Text(stringResource(preference.title))
            preference.subtitle?.let { subtitle -> Text(subtitle) }
        }
    }
}