package com.faltenreich.diaguard.backup.user.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.backup.user.BackupStepListItem
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_write
import diaguard.shared.generated.resources.backup_write_description
import diaguard.shared.generated.resources.start
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
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.backup_write_description))

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        BackupStepListItem(
            index = 0,
            label = stringResource(Res.string.backup_write),
        ) {
            Button(onClick = { viewModel.dispatchIntent(WriteBackupFormIntent.Confirm) }) {
                Text(stringResource(Res.string.start))
            }
        }
    }
}