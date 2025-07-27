package com.faltenreich.diaguard.navigation.bar.bottom

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview
@Composable
private fun Preview() = AppPreview {
    BottomAppBarItem(
        painter = painterResource(Res.drawable.ic_search),
        contentDescription = Res.string.search_open,
        onClick = {},
    )
}