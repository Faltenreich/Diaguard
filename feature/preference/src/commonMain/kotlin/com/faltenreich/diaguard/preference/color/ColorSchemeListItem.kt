package com.faltenreich.diaguard.preference.color

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.data.preference.color.ColorScheme
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.theme.AppTheme
import diaguard.feature.preference.generated.resources.Res
import diaguard.feature.preference.generated.resources.color_scheme_dark
import diaguard.feature.preference.generated.resources.color_scheme_light
import diaguard.feature.preference.generated.resources.color_scheme_system
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ColorSchemeListItem(
    colorScheme: ColorScheme,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(all = AppTheme.dimensions.padding.P_3_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(
                when (colorScheme) {
                    ColorScheme.SYSTEM -> Res.string.color_scheme_system
                    ColorScheme.LIGHT -> Res.string.color_scheme_light
                    ColorScheme.DARK -> Res.string.color_scheme_dark
                }
            ),
            modifier = Modifier.weight(1f),
        )
        RadioButton(
            selected = isSelected,
            onClick = null,
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    ColorSchemeListItem(
        colorScheme = ColorScheme.SYSTEM,
        isSelected = true,
        onClick = {},
    )
}