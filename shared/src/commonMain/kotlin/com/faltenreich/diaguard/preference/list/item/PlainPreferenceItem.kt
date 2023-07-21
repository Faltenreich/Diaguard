package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.list.Preference

@Composable
fun PlainPreferenceItem(
    preference: Preference.Plain,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
        Text(preference.title)
        preference.subtitle?.let { subtitle -> Text(subtitle) }
    }
}