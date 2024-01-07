package com.faltenreich.diaguard.entry.form.tag

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.tag.Tag

@Composable
fun EntryTagItem(
    tag: Tag,
    onClick: (Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    AssistChip(
        onClick = { onClick(tag) },
        label = { Text(tag.name) },
        modifier = modifier,
        trailingIcon = {
            ResourceIcon(
                imageResource = MR.images.ic_clear,
                contentDescription = getString(MR.strings.tag_remove_description, tag.name),
                modifier = Modifier.size(InputChipDefaults.AvatarSize),
            )
        },
    )
}