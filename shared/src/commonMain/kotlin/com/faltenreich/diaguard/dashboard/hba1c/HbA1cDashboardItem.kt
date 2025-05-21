package com.faltenreich.diaguard.dashboard.hba1c

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.entry.Entry
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c
import diaguard.shared.generated.resources.hba1c_estimated
import diaguard.shared.generated.resources.hba1c_latest
import diaguard.shared.generated.resources.placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun HbA1cDashboardItem(
    state: DashboardState.HbA1c,
    onOpenEntry: (Entry.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showEstimatedHbA1cBottomSheet by remember { mutableStateOf(false) }

    Card(
        onClick = {
            when (state) {
                is DashboardState.HbA1c.Latest -> onOpenEntry(state.entry)
                is DashboardState.HbA1c.Estimated,
                is DashboardState.HbA1c.Unknown -> showEstimatedHbA1cBottomSheet = true
            }
        },
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = when (state) {
                    is DashboardState.HbA1c.Latest -> stringResource(Res.string.hba1c_latest, state.dateTime)
                    is DashboardState.HbA1c.Estimated -> stringResource(Res.string.hba1c_estimated)
                    is DashboardState.HbA1c.Unknown -> stringResource(Res.string.hba1c)
                },
                style = AppTheme.typography.labelMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                when (state) {
                    is DashboardState.HbA1c.Latest -> state.value.value
                    is DashboardState.HbA1c.Estimated -> state.value.value
                    is DashboardState.HbA1c.Unknown -> stringResource(Res.string.placeholder)
                }
            )
        }
    }

    if (showEstimatedHbA1cBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEstimatedHbA1cBottomSheet = false },
        ) {
            EstimatedHbA1cInfo()
        }
    }
}