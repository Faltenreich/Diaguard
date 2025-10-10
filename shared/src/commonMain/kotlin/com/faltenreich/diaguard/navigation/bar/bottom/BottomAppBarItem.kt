package com.faltenreich.diaguard.navigation.bar.bottom

import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.faltenreich.diaguard.shared.view.preview.AppPreview
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

    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(contentDescription) } },
        state = tooltipState,
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
private fun Preview() = AppPreview {
    BottomAppBarItem(
        painter = painterResource(Res.drawable.ic_search),
        contentDescription = "",
        onClick = {},
    )
}