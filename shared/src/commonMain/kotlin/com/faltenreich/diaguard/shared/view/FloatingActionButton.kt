package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_add
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FloatingActionButton(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shapes.large,
    containerColor: Color = AppTheme.colors.scheme.onPrimary,
    contentColor: Color = AppTheme.colors.scheme.primary,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
) {
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState()

    Tooltip(
        text = contentDescription,
        state = tooltipState,
        modifier = modifier,
    ) {
        androidx.compose.material3.FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.combinedClickable(
                onClick = onClick,
                onLongClick = { scope.launch { tooltipState.show() } },
            ),
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            elevation = elevation,
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
    FloatingActionButton(
        painter = painterResource(Res.drawable.ic_add),
        contentDescription = "",
        onClick = {},
    )
}