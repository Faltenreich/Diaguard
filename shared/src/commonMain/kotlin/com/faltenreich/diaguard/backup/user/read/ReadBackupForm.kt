package com.faltenreich.diaguard.backup.user.read

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.wizard.WizardStepListItem
import com.faltenreich.diaguard.shared.wizard.WizardStepState
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_read_completed
import diaguard.shared.generated.resources.backup_read_confirm
import diaguard.shared.generated.resources.backup_read_idle
import diaguard.shared.generated.resources.backup_read_loading
import diaguard.shared.generated.resources.backup_read_preview
import diaguard.shared.generated.resources.backup_read_start
import diaguard.shared.generated.resources.backup_read_summary
import diaguard.shared.generated.resources.select
import diaguard.shared.generated.resources.start
import diaguard.shared.generated.resources.store
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
        Text(stringResource(Res.string.backup_read_summary))

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
            state = WizardStepState.UPCOMING,
        ) {
            Button(onClick = { viewModel.dispatchIntent(ReadBackupFormIntent.Read) }) {
                Text(stringResource(Res.string.start))
            }
        }

        WizardStepListItem(
            index = 2,
            label = stringResource(Res.string.backup_read_loading),
            state = WizardStepState.UPCOMING,
        ) {
            CircularProgressIndicator(
                color = AppTheme.colors.scheme.onPrimaryContainer,
            )
        }

        WizardStepListItem(
            index = 3,
            label = stringResource(Res.string.backup_read_preview),
            state = WizardStepState.UPCOMING,
        ) {
            Button(onClick = { viewModel.dispatchIntent(ReadBackupFormIntent.Check) }) {
                Text(stringResource(Res.string.start))
            }
        }

        WizardStepListItem(
            index = 4,
            label = stringResource(Res.string.backup_read_confirm),
            state = WizardStepState.UPCOMING,
        ) {
            Button(onClick = { viewModel.dispatchIntent(ReadBackupFormIntent.Store) }) {
                Text(stringResource(Res.string.store))
            }
        }

        WizardStepListItem(
            index = 5,
            label = stringResource(Res.string.backup_read_completed),
            state = WizardStepState.UPCOMING,
        ) {}
    }
}