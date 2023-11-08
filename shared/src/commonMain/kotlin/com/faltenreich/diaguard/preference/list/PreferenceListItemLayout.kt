package com.faltenreich.diaguard.preference.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun PreferenceListItemLayout(
    preference: Preference,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = AppTheme.dimensions.size.ListItemHeightMinimum)
            .padding(all = AppTheme.dimensions.padding.P_3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(AppTheme.dimensions.size.ListOffsetWidth))
        Column {
            Column {
                Text(getString(preference.title))
                preference.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = AppTheme.typography.bodySmall,
                    )
                }
            }
        }
        content?.invoke()
    }
}