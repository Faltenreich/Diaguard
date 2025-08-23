package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.format
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun ReminderPicker(
    duration: Duration,
    onChange: (Duration) -> Unit,
    modifier: Modifier = Modifier,
) {
    var numbers by remember {
        mutableStateOf(
            duration.toComponents { hours, minutes, seconds, _ ->
                val string = "%02d%02d%02d".format(hours, minutes, seconds)
                string.toCharArray().map(Char::digitToInt)
            }
        )
    }
    val styleDigit = AppTheme.typography.displayMedium.toSpanStyle().copy(
        fontFeatureSettings = "tnum",
    )
    // TODO: Localize labels
    val styleLabel = AppTheme.typography.titleMedium.toSpanStyle().copy(
        fontFeatureSettings = "tnum",
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(styleDigit) {
                    append(numbers[0].toString())
                    append(numbers[1].toString())
                }
                withStyle(styleLabel) {
                    append("h")
                    append(" ")
                }
                withStyle(styleDigit) {
                    append(numbers[2].toString())
                    append(numbers[3].toString())
                }
                withStyle(styleLabel) {
                    append("m")
                    append(" ")
                }
                withStyle(styleDigit) {
                    append(numbers[4].toString())
                    append(numbers[5].toString())
                }
                withStyle(styleLabel) {
                    append("s")
                }
            },
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
        ) {
            items(9) { index ->
                Button(
                    text = "${index + 1}",
                    onClick = {},
                )
            }
            item {
                Button(
                    text = "x",
                    onClick = {},
                )
            }
            item {
                Button(
                    text = "0",
                    onClick = {},
                )
            }
            item {
                Button(
                    text = "<",
                    onClick = {},
                )
            }
        }
    }
}

@Composable
private fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = AppTheme.colors.scheme.surfaceContainerLowest,
            contentColor = AppTheme.colors.scheme.onSurface,
        ),
        shape = CircleShape,
    ) {
        Text(
            text = text,
            style = AppTheme.typography.headlineMedium,
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderPicker(
        duration = 1.hours + 2.minutes + 3.seconds,
        onChange = {},
    )
}