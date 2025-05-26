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
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.statistic.StatisticIntent

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