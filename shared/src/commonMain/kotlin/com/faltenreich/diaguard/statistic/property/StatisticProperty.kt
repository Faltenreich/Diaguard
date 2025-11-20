package com.faltenreich.diaguard.statistic.property

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.view.Divider
import com.faltenreich.diaguard.view.FormRow
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.statistic.StatisticIntent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatisticProperty(
    state: StatisticPropertyState?,
    onIntent: (StatisticIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        val properties = state?.properties ?: emptyList()
        if (properties.isNotEmpty()) {
            Divider()
        }
        properties.forEach { property ->
            val isSelected = property == state?.selection
            FormRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = { onIntent(StatisticIntent.SetProperty(property)) },
                    ),
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = null,
                    modifier = Modifier.padding(horizontal = AppTheme.dimensions.padding.P_0_5),
                )
                Text(property.name)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val properties = listOf(
        property(),
        property(),
    )
    StatisticProperty(
        state = StatisticPropertyState(
            properties = properties,
            selection = properties.first(),
        ),
        onIntent = {},
    )
}