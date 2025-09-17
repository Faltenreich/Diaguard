package com.faltenreich.diaguard.dashboard.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_alarm
import diaguard.shared.generated.resources.ic_delete
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardReminder(
    state: DashboardReminderState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) = with(state) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = AppTheme.dimensions.padding.P_3),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ResourceIcon(
                icon = Res.drawable.ic_alarm,
                modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
            )
            Text(
                text = text,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onDelete) {
                ResourceIcon(
                    icon = Res.drawable.ic_delete,
                    modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    DashboardReminder(
        state = DashboardReminderState(
            text = "Reminder in 5 minutes"
        ),
        onDelete = {},
    )
}