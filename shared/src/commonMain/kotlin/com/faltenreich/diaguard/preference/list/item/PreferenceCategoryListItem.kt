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
import com.faltenreich.diaguard.shared.localization.getString
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource

class PreferenceCategoryListItem(
    title: StringResource,
    private val icon: ImageResource,
) : PreferenceListItem(title, null) {

    @Composable
    override fun Content(modifier: Modifier) {
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
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
                    colorFilter = ColorFilter.tint(AppTheme.colors.scheme.primary),
                )
            }
            Text(
                text = getString(title),
                color = AppTheme.colors.scheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}