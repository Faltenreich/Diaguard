package com.faltenreich.diaguard.backup.user.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.wizard.WizardStepListItem
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_write_complete
import diaguard.shared.generated.resources.backup_write_description
import diaguard.shared.generated.resources.backup_write_progress
import diaguard.shared.generated.resources.backup_write_start
import diaguard.shared.generated.resources.start
import diaguard.shared.generated.resources.store
import org.jetbrains.compose.resources.stringResource

@Composable
fun WriteBackupForm(
    modifier: Modifier = Modifier,
    viewModel: WriteBackupFormViewModel,
) {
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
            label = stringResource(Res.string.backup_write_start),
        ) {
            Button(onClick = { viewModel.dispatchIntent(WriteBackupFormIntent.Store) }) {
                Text(stringResource(Res.string.start))
            }
        }

        WizardStepListItem(
            index = 1,
            label = stringResource(Res.string.backup_write_progress),
        ) {
            CircularProgressIndicator()
        }

        WizardStepListItem(
            index = 2,
            label = stringResource(Res.string.backup_write_complete),
        ) {
            Button(onClick = { viewModel.dispatchIntent(WriteBackupFormIntent.Store) }) {
                Text(stringResource(Res.string.store))
            }
        }
    }
}