package com.faltenreich.diaguard.entry.form.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.tag.Tag

@Composable
fun EntryTagList(
    tags: List<Tag>,
    onTagClick: (Tag) -> Unit,
    trailingIcon: @Composable (Tag) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.padding(horizontal = AppTheme.dimensions.padding.P_3),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
    ) {
        tags.forEach { tag ->
            EntryTagItem(
                tag = tag,
                onClick = onTagClick,
                trailingIcon = trailingIcon,
            )
        }
    }
}