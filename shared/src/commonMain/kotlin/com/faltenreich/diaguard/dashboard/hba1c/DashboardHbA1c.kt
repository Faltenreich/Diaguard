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
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c
import diaguard.shared.generated.resources.hba1c_estimated
import diaguard.shared.generated.resources.hba1c_latest
import diaguard.shared.generated.resources.placeholder
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardHbA1c(
    state: DashboardHbA1cState?,
    onOpenEntry: (Entry.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showEstimatedHbA1cBottomSheet by remember { mutableStateOf(false) }

    Card(
        onClick = {
            when (state) {
                is DashboardHbA1cState.Latest -> onOpenEntry(state.entry)
                is DashboardHbA1cState.Estimated,
                is DashboardHbA1cState.Unknown,
                null-> showEstimatedHbA1cBottomSheet = true
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
                    is DashboardHbA1cState.Latest -> stringResource(Res.string.hba1c_latest, state.dateTime)
                    is DashboardHbA1cState.Estimated -> stringResource(Res.string.hba1c_estimated)
                    is DashboardHbA1cState.Unknown,
                    null -> stringResource(Res.string.hba1c)
                },
                style = AppTheme.typography.labelMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                when (state) {
                    is DashboardHbA1cState.Latest -> state.value.value
                    is DashboardHbA1cState.Estimated -> state.value.value
                    is DashboardHbA1cState.Unknown,
                    null -> stringResource(Res.string.placeholder)
                }
            )
        }
    }

    if (showEstimatedHbA1cBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showEstimatedHbA1cBottomSheet = false }) {
            DashboardHbA1cEstimatedInfo()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppPreview {
        DashboardHbA1c(
            state = DashboardHbA1cState.Estimated(
                value = MeasurementValue.Localized(
                    value = "value",
                ),
            ),
            onOpenEntry = {},
        )
    }
}

@Preview
@Composable
private fun PreviewLatest() = AppPreview {
    DashboardHbA1c(
        state = DashboardHbA1cState.Latest(
            entry = entry(),
            dateTime = "dateTime",
            value = MeasurementValue.Localized(
                value = "value",
            ),
        ),
        onOpenEntry = {},
    )
}