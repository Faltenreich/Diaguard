package com.faltenreich.diaguard.entry.form.tag

import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.data.tag.Tag
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
private fun Preview() = PreviewScaffold {
    EntryTagListItem(
        tag = tag(),
        onClick = {},
        trailingIcon = {},
    )
}