package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.PreferenceListItemScaffold
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PreferenceActionListItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    PreferenceListItemScaffold(
        title = title ,
        subtitle = subtitle,
        modifier = modifier.clickable { onClick() },
    )
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    PreferenceActionListItem(
        title = "Title",
        onClick = {},
        subtitle = "Subtitle",
    )
}