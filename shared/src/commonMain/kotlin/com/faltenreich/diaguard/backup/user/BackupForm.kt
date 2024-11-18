package com.faltenreich.diaguard.backup.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.Divider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_read
import diaguard.shared.generated.resources.backup_read_description
import diaguard.shared.generated.resources.backup_write
import diaguard.shared.generated.resources.backup_write_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackupForm(
    modifier: Modifier = Modifier,
    viewModel: BackupFormViewModel,
) {
    Column(modifier = modifier) {
        BackupFormListItem(
            title = stringResource(Res.string.backup_write),
            description = stringResource(Res.string.backup_write_description),
            modifier = Modifier.clickable { viewModel.dispatchIntent(BackupFormIntent.Write) },
        )
        Divider()
        BackupFormListItem(
            title = stringResource(Res.string.backup_read),
            description = stringResource(Res.string.backup_read_description),
            modifier = Modifier.clickable { viewModel.dispatchIntent(BackupFormIntent.Read) },
        )
    }
}