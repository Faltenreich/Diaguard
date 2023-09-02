package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun MeasurementPropertyListItem(
    property: MeasurementProperty,
    onArrowUp: (MeasurementProperty) -> Unit,
    showArrowUp: Boolean,
    onArrowDown: (MeasurementProperty) -> Unit,
    showArrowDown: Boolean,
    modifier: Modifier = Modifier,
) {
    val recentlyCreated = (DateTime.now().millisSince1970 - property.createdAt.millisSince1970) < 5.seconds.inWholeMilliseconds
    val startColor = if (recentlyCreated) AppTheme.colors.material.surfaceVariant else Color.Transparent
    val endColor = Color.Transparent
    val color = remember { Animatable(startColor) }
    LaunchedEffect(Unit) {
        delay(5.seconds)
        color.animateTo(endColor, animationSpec = tween())
    }

    Column(modifier = modifier) {
        FormRow(
            icon = { MeasurementPropertyIcon(property) },
            modifier = Modifier.background(color.value),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(property.name)
                property.types.takeIf { it.size > 1 }?.joinToString { it.name }?.let { typeNames ->
                    Text(
                        text = typeNames,
                        style = AppTheme.typography.bodySmall,
                    )
                }
            }
            IconButton(
                onClick = { onArrowUp(property) },
                modifier = Modifier.alpha(if (showArrowUp) 1f else 0f),
            ) {
                ResourceIcon(MR.images.ic_arrow_up)
            }
            IconButton(
                onClick = { onArrowDown(property) },
                modifier = Modifier.alpha(if (showArrowDown) 1f else 0f),
            ) {
                ResourceIcon(MR.images.ic_arrow_down)
            }
        }
        Divider()
    }
}