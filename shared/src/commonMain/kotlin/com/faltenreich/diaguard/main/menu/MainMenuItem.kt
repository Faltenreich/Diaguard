package com.faltenreich.diaguard.main.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainMenuItem(
    label: StringResource,
    icon: DrawableResource?,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3_5,
                vertical = AppTheme.dimensions.padding.P_3,
            ),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3_5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val onPrimaryColor =
            if (isActive) AppTheme.colors.scheme.primary
            else AppTheme.colors.scheme.onBackground
        icon?.let {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimensions.padding.P_4),
                colorFilter = ColorFilter.tint(onPrimaryColor),
            )
        } ?: Spacer(modifier = Modifier.size(AppTheme.dimensions.padding.P_4))
        Text(
            text = getString(label),
            color = onPrimaryColor,
        )
    }
}