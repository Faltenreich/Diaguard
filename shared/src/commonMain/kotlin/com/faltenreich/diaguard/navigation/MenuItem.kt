package com.faltenreich.diaguard.navigation

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

@Composable
fun MenuItem(
    icon: ImageResource,
    label: StringResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(icon.drawableResId),
            contentDescription = null,
            modifier = Modifier.size(36.dp),
            colorFilter = ColorFilter.tint(Color.DarkGray),
        )
        Text(
            text = stringResource(label.resourceId)
        )
    }
}

@Preview
@Composable
private fun MenuItemPreview() {
    MenuItem(
        icon = MR.images.ic_dashboard,
        label = MR.strings.dashboard,
        onClick = {},
    )
}