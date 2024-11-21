package com.faltenreich.diaguard.backup.user.read

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.wizard.WizardStepListItem
import com.faltenreich.diaguard.shared.wizard.WizardStepState
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_read_completed
import diaguard.shared.generated.resources.backup_read_description
import diaguard.shared.generated.resources.backup_read_idle
import diaguard.shared.generated.resources.backup_read_loading
import diaguard.shared.generated.resources.backup_read_start
import diaguard.shared.generated.resources.select
import diaguard.shared.generated.resources.start
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReadBackupForm(
    modifier: Modifier = Modifier,
    viewModel: ReadBackupFormViewModel,
) {
    val state = viewModel.collectState() ?: return

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3_5,
                vertical = AppTheme.dimensions.padding.P_3,
            ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.backup_read_description))

        WizardStepListItem(
            index = 0,
            label = stringResource(Res.string.backup_read_idle),
            state =
            if (state == ReadBackupFormState.Idle) WizardStepState.CURRENT
            else WizardStepState.COMPLETED,
        ) {
            Button(onClick = { viewModel.dispatchIntent(ReadBackupFormIntent.Select) }) {
                Text(stringResource(Res.string.select))
            }
        }

        WizardStepListItem(
            index = 1,
            label = stringResource(Res.string.backup_read_start),
            state = when (state) {
                is ReadBackupFormState.Idle -> WizardStepState.UPCOMING
                is ReadBackupFormState.Ready -> WizardStepState.CURRENT
                else -> WizardStepState.COMPLETED
            },
        ) {
            Button(onClick = { viewModel.dispatchIntent(ReadBackupFormIntent.Start) }) {
                Text(stringResource(Res.string.start))
            }
        }

        WizardStepListItem(
            index = 1,
            label = stringResource(Res.string.backup_read_loading),
            state = when (state) {
                is ReadBackupFormState.Idle,
                is ReadBackupFormState.Ready -> WizardStepState.UPCOMING
                is ReadBackupFormState.Loading -> WizardStepState.CURRENT
                else -> WizardStepState.COMPLETED
            },
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        WizardStepListItem(
            index = 1,
            label = stringResource(Res.string.backup_read_completed),
            state = when (state) {
                is ReadBackupFormState.Completed,
                is ReadBackupFormState.Error -> WizardStepState.CURRENT
                else -> WizardStepState.UPCOMING
            },
        ) {}
    }
}