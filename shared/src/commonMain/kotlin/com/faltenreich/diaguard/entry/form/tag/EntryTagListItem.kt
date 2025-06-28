package com.faltenreich.diaguard.entry.form.tag

import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.tag.Tag
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntryTagListItem(
    tag: Tag,
    onClick: (Tag) -> Unit,
    trailingIcon: @Composable (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedAssistChip(
        onClick = { onClick(tag) },
        label = { Text(tag.name) },
        modifier = modifier,
        trailingIcon = { trailingIcon(tag) },
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    EntryTagListItem(
        tag = tag(),
        onClick = {},
        trailingIcon = {},
    )
}