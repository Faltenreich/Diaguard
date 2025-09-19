package com.faltenreich.diaguard.log.list.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.log.LogIntent
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.no_entries
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogEmpty(
    state: LogItemState.EmptyContent,
    onIntent: (LogIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        LogDay(
            date = state.date,
            style = state.style,
            modifier = Modifier.width(AppTheme.dimensions.size.LogDayWidth),
        )
        Card(
            onClick = { onIntent(LogIntent.CreateEntry(state.date)) },
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.Transparent),
        ) {
            Text(
                text = getString(Res.string.no_entries),
                color = TextFieldDefaults.colors().disabledLabelColor,
                modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    LogEmpty(
        state = LogItemState.EmptyContent(
            date = today(),
            style = LogDayStyle(
                isVisible = true,
                isHighlighted = true,
            ),
        ),
        onIntent = {},
    )
}