package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.list.Preference
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun CategoryPreferenceListItem(
    preference: Preference.Category,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier
        .padding(all = AppTheme.dimensions.padding.P_3)
        .clickable {},
    ) {
        Image(
            painter = painterResource(preference.icon),
            contentDescription = null,
            modifier = Modifier.size(AppTheme.dimensions.size.ImageMedium),
            colorFilter = ColorFilter.tint(AppTheme.colors.material.primary),
        )
        Text(stringResource(preference.title))
    }
}