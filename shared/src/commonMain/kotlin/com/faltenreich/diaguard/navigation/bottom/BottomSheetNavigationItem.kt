package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import com.faltenreich.diaguard.AppTheme
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun BottomSheetNavigationItem(
    icon: ImageResource,
    label: StringResource,
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
            if (isActive) AppTheme.colors.Material.primary
            else AppTheme.colors.Material.onBackground
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(AppTheme.dimensions.padding.P_4),
            colorFilter = ColorFilter.tint(onPrimaryColor),
        )
        Text(
            text = stringResource(label),
            color = onPrimaryColor,
        )
    }
}