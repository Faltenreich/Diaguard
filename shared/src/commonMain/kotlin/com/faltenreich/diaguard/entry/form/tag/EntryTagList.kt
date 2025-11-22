package com.faltenreich.diaguard.entry.form.tag

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.data.preview.AppPreview
import com.faltenreich.diaguard.data.tag.Tag
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntryTagList(
    tags: Collection<Tag>,
    onTagClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (Tag) -> Unit = {},
) {
    FlowRow(
        modifier = modifier.animateContentSize(),
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