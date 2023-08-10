package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.list.Preference
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun CategoryPreferenceListItem(
    preference: Preference.Category,
    modifier: Modifier = Modifier,
) {
    Divider()
    Row(
        modifier = modifier
            .padding(
                start = AppTheme.dimensions.padding.P_3,
                top = AppTheme.dimensions.padding.P_3,
                end = AppTheme.dimensions.padding.P_3,
                bottom = AppTheme.dimensions.padding.P_2,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.width(AppTheme.dimensions.size.ListOffsetWidth)) {
            Image(
                painter = painterResource(preference.icon),
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
                colorFilter = ColorFilter.tint(AppTheme.colors.material.primary),
            )
        }
        Text(
            text = stringResource(preference.title),
            color = AppTheme.colors.material.primary,
            fontWeight = FontWeight.Bold,
        )
    }
}