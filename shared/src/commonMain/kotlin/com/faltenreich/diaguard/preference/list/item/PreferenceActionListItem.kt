package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.PreferenceListItemScaffold

@Composable
fun PreferenceActionListItem(
    title: String,
    onClick: () -> Unit,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    PreferenceListItemScaffold(
        title = title ,
        subtitle = subtitle,
        modifier = modifier.clickable { onClick() },
    )
}