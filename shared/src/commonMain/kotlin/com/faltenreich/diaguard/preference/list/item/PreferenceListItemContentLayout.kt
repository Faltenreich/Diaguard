package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.Preference
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun PreferenceContentLayout(
    preference: Preference,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
) {
    Row(modifier = modifier) {
        Column {
            Column {
                Text(stringResource(preference.title))
                preference.subtitle?.let { subtitle -> Text(subtitle) }
            }
        }
        content?.invoke()
    }
}