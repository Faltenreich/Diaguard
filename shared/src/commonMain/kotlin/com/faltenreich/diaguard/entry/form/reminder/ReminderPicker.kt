package com.faltenreich.diaguard.entry.form.reminder

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
    Column(modifier = modifier) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) {
                    append(numbers[0].toString())
                    append(numbers[1].toString())
                    append("h")
                    append(numbers[2].toString())
                    append(numbers[3].toString())
                    append("m")
                    append(numbers[4].toString())
                    append(numbers[5].toString())
                    append("s")
                }
            },
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