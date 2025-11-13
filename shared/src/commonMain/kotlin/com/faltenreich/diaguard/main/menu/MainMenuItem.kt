package com.faltenreich.diaguard.main.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.screen.StartScreen
import com.faltenreich.diaguard.core.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainMenuItem(
    label: StringResource,
    icon: DrawableResource?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppTheme.dimensions.padding.P_2,
                vertical = AppTheme.dimensions.padding.P_1,
            )
            .background(
                color =
                    if (isSelected && icon != null) AppTheme.colors.scheme.surfaceContainerLowest
                    else Color.Transparent,
                shape = AppTheme.shapes.large,
            )
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3,
                vertical = AppTheme.dimensions.padding.P_2_5,
            ),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val onPrimaryColor =
            if (isSelected) AppTheme.colors.scheme.primary
            else AppTheme.colors.scheme.onBackground
        icon?.let {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimensions.padding.P_4),
                tint = onPrimaryColor,
            )
        } ?: Spacer(modifier = Modifier.size(AppTheme.dimensions.padding.P_4))
        Text(
            text = getString(label),
            color = onPrimaryColor,
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MainMenuItem(
        label = StartScreen.DASHBOARD.labelResource,
        icon = StartScreen.DASHBOARD.iconResource,
        isSelected = true,
        onClick = {},
    )
}