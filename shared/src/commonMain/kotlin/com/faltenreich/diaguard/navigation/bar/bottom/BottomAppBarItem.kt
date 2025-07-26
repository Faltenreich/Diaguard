package com.faltenreich.diaguard.navigation.bar.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.faltenreich.diaguard.shared.localization.getString
import org.jetbrains.compose.resources.StringResource

@Composable
fun BottomAppBarItem(
    painter: Painter,
    contentDescription: StringResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painter,
            contentDescription = getString(contentDescription),
        )
    }
}