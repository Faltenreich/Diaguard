package com.faltenreich.diaguard.tag.list

import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.tag.Tag

@Composable
fun TagListItem(
    tag: Tag,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        Text(
            text = tag.name,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = { onDelete() }) {
            ResourceIcon(
                imageResource = MR.images.ic_delete,
                contentDescription = getString(MR.strings.tag_delete),
            )
        }
    }
}