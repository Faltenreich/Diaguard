package com.faltenreich.diaguard.preference.list

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun PreferenceCheckBoxListItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    PreferenceListItemScaffold(
        title = title,
        subtitle = subtitle,
        modifier = modifier.clickable { onCheckedChange(!isChecked) },
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(!isChecked) },
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    PreferenceCheckBoxListItem(
        title = "Title",
        isChecked = true,
        onCheckedChange = {},
    )
}