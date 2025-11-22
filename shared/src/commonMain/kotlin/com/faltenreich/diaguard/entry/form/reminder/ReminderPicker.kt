@file:Suppress("MagicNumber")

package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.localization.NumberFormatter
import com.faltenreich.diaguard.view.image.ResourceIcon
import com.faltenreich.diaguard.data.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hours_abbreviation_short
import diaguard.shared.generated.resources.ic_backspace
import diaguard.shared.generated.resources.minutes_abbreviation_short
import diaguard.shared.generated.resources.seconds_abbreviation_short
import org.jetbrains.compose.resources.stringResource
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
    numberFormatter: NumberFormatter = inject(),
) {
    var numbers by remember {
        mutableStateOf(
            duration.toComponents { hours, minutes, seconds, _ ->
                // TODO: Format via UseCase
                val format = { number: Int -> numberFormatter.invoke(number, width = 2, padZeroes = true) }
                val string = "${format(hours.toInt())}${format(minutes)}${format(seconds)}"
                string.toCharArray().map(Char::digitToInt)
            }
        )
    }

    val latestOnChange by rememberUpdatedState(onChange)
    LaunchedEffect(numbers) {
        val hours = numbers.subList(0, 2).joinToString("").toInt()
        val minutes = numbers.subList(2, 4).joinToString("").toInt()
        val seconds = numbers.subList(4, 6).joinToString("").toInt()
        val duration = hours.hours + minutes.minutes + seconds.seconds
        latestOnChange(duration)
    }

    val styleDigit = AppTheme.typography.displayMedium.toSpanStyle().copy(fontFamily = FontFamily.Monospace)
    val styleLabel = AppTheme.typography.titleMedium.toSpanStyle().copy(fontFamily = FontFamily.Monospace)
    val styleButton = AppTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Monospace)
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
                    append(stringResource(Res.string.hours_abbreviation_short))
                    append(" ")
                }
                withStyle(styleDigit) {
                    append(numbers[2].toString())
                    append(numbers[3].toString())
                }
                withStyle(styleLabel) {
                    append(stringResource(Res.string.minutes_abbreviation_short))
                    append(" ")
                }
                withStyle(styleDigit) {
                    append(numbers[4].toString())
                    append(numbers[5].toString())
                }
                withStyle(styleLabel) {
                    append(stringResource(Res.string.seconds_abbreviation_short))
                }
            },
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
        ) {
            items(9) { index ->
                val number = index + 1
                Button(onClick = { numbers = numbers.add(number) }) {
                    Text(
                        text = number.toString(),
                        style = styleButton,
                    )
                }
            }
            item {
                Button(onClick = { numbers = numbers.add(0).add(0) }) {
                    Text(
                        text = "00",
                        style = styleButton,
                    )
                }
            }
            item {
                Button(onClick = { numbers = numbers.add(0) }) {
                    Text(
                        text = 0.toString(),
                        style = styleButton,
                    )
                }
            }
            item {
                Button(onClick = { numbers = numbers.removeLast() }) {
                    ResourceIcon(Res.drawable.ic_backspace)
                }
            }
        }
    }
}

private fun List<Int>.add(number: Int): List<Int> {
    return if (first() == 0) subList(1, size) + number else this
}

private fun List<Int>.removeLast(): List<Int> {
    return listOf(0) + subList(0, lastIndex)
}

@Composable
private fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = AppTheme.colors.scheme.surfaceContainerLowest,
            contentColor = AppTheme.colors.scheme.onSurface,
        ),
        shape = CircleShape,
        content = content,
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    ReminderPicker(
        duration = 1.hours + 2.minutes + 3.seconds,
        onChange = {},
    )
}