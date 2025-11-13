package com.faltenreich.diaguard.preference.color

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.core.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.color_scheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ColorSchemeForm(
    state: ColorScheme,
    onChange: (ColorScheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString(Res.string.color_scheme),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))

        Column(modifier = Modifier.selectableGroup()) {
            ColorScheme.entries.forEach { colorScheme ->
                ColorSchemeListItem(
                    colorScheme = colorScheme,
                    isSelected = state == colorScheme,
                    onClick = { onChange(colorScheme) },
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ColorSchemeForm(
        state = ColorScheme.SYSTEM,
        onChange = {},
    )
}