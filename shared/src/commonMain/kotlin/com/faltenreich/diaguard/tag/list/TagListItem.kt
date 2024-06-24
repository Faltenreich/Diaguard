package com.faltenreich.diaguard.tag.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.tag.Tag

@Composable
fun TagListItem(
    tag: Tag,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        Text(tag.name)
    }
}