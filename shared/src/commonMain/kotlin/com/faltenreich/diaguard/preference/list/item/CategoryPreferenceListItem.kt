package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    PreferenceListItem(
        modifier = modifier,
        top = { Divider() },
        left = {
            Image(
                painter = painterResource(preference.icon),
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
                colorFilter = ColorFilter.tint(AppTheme.colors.material.primary),
            )
        },
        content = {
            Text(
                text = stringResource(preference.title),
                color = AppTheme.colors.material.primary,
                fontWeight = FontWeight.Bold,
            )
        }
    )
}