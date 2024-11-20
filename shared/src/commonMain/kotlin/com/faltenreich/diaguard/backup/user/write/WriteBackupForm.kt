package com.faltenreich.diaguard.backup.user.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.ExtendedFloatingActionButton
import com.faltenreich.diaguard.shared.wizard.WizardStepListItem
import com.faltenreich.diaguard.shared.wizard.WizardStepState
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_write_completed
import diaguard.shared.generated.resources.backup_write_description
import diaguard.shared.generated.resources.backup_write_idle
import diaguard.shared.generated.resources.backup_write_loading
import diaguard.shared.generated.resources.retry
import diaguard.shared.generated.resources.start
import diaguard.shared.generated.resources.store
import org.jetbrains.compose.resources.stringResource

@Composable
fun WriteBackupForm(
    modifier: Modifier = Modifier,
    viewModel: WriteBackupFormViewModel,
) {
    val state = viewModel.collectState() ?: WriteBackupFormState.Idle

    Column(
        modifier = modifier.padding(
            horizontal = AppTheme.dimensions.padding.P_3_5,
            vertical = AppTheme.dimensions.padding.P_3,
        ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.backup_write_description))

        WizardStepListItem(
            index = 0,
            label = stringResource(Res.string.backup_write_idle),
            state =
                if (state == WriteBackupFormState.Idle) WizardStepState.CURRENT
                else WizardStepState.COMPLETED,
        )

        WizardStepListItem(
            index = 1,
            label = stringResource(Res.string.backup_write_loading),
            state = when (state) {
                WriteBackupFormState.Idle -> WizardStepState.UPCOMING
                WriteBackupFormState.Loading -> WizardStepState.CURRENT
                WriteBackupFormState.Completed,
                WriteBackupFormState.Error -> WizardStepState.COMPLETED
            },
        )

        WizardStepListItem(
            index = 2,
            label = stringResource(Res.string.backup_write_completed),
            state = when (state) {
                WriteBackupFormState.Idle,
                WriteBackupFormState.Loading -> WizardStepState.UPCOMING
                WriteBackupFormState.Completed,
                WriteBackupFormState.Error -> WizardStepState.CURRENT
            },
        )

        ExtendedFloatingActionButton(
            text = {
                when (state) {
                    WriteBackupFormState.Idle -> Text(stringResource(Res.string.start))
                    WriteBackupFormState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.size(AppTheme.dimensions.size.ImageSmall),
                    )
                    WriteBackupFormState.Completed -> Text(stringResource(Res.string.store))
                    WriteBackupFormState.Error -> Text(stringResource(Res.string.retry))
                }
            },
            onClick = {
                when (state) {
                    WriteBackupFormState.Idle -> viewModel.dispatchIntent(WriteBackupFormIntent.Start)
                    WriteBackupFormState.Loading -> Unit
                    WriteBackupFormState.Completed -> viewModel.dispatchIntent(WriteBackupFormIntent.Store)
                    WriteBackupFormState.Error -> viewModel.dispatchIntent(WriteBackupFormIntent.Start)
                }
            },
        )
    }
}