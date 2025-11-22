package com.faltenreich.diaguard.navigation.bar.bottom

import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.faltenreich.diaguard.view.info.Tooltip
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_search
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomAppBarItem(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState()

    Tooltip(
        text = contentDescription,
        state = tooltipState,
        modifier = modifier,
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.combinedClickable(
                onClick = onClick,
                onLongClick = { scope.launch { tooltipState.show() } },
            ),
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    BottomAppBarItem(
        painter = painterResource(Res.drawable.ic_search),
        contentDescription = "",
        onClick = {},
    )
}