package com.faltenreich.diaguard.entry.form.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.tag.Tag
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntryTagList(
    tags: List<Tag>,
    onTagClick: (Tag) -> Unit,
    trailingIcon: @Composable (Tag) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
    ) {
        tags.forEach { tag ->
            EntryTagListItem(
                tag = tag,
                onClick = onTagClick,
                trailingIcon = trailingIcon,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    EntryTagList(
        tags = listOf(tag()),
        onTagClick = {},
        trailingIcon = {},
    )
}