package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.faltenreich.diaguard.shared.localization.getString
import dev.icerock.moko.resources.StringResource

@Composable
fun BottomAppBarItem(
    painter: Painter,
    contentDescription: StringResource,
    onClick: () -> Unit,
){
    IconButton(onClick = onClick) {
        Icon(painter, getString(contentDescription))
    }
}