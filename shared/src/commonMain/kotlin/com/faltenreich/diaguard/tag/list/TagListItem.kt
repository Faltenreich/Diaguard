package com.faltenreich.diaguard.tag.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.FormRow
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.tag.Tag
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TagListItem(
    tag: Tag,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        Text(tag.name)
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    TagListItem(tag = tag())
}