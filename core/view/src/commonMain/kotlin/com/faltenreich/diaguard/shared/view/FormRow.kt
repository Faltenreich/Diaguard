package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import diaguard.core.view.generated.resources.Res
import diaguard.core.view.generated.resources.ic_note
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FormRow(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeMedium)
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3,
                vertical = AppTheme.dimensions.padding.P_1,
            ),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.invoke()
        content()
    }
}

@Preview
@Composable
private fun Preview() {
    FormRow(
        icon = { ResourceIcon(Res.drawable.ic_note) },
        content = { Text("FormRow") },
    )
}