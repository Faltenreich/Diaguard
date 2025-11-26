package com.faltenreich.diaguard.backup.read

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
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.backup_read_completed
import com.faltenreich.diaguard.resource.backup_read_confirm
import com.faltenreich.diaguard.resource.backup_read_idle
import com.faltenreich.diaguard.resource.backup_read_loading
import com.faltenreich.diaguard.resource.backup_read_preview
import com.faltenreich.diaguard.resource.backup_read_start
import com.faltenreich.diaguard.resource.backup_read_summary
import com.faltenreich.diaguard.resource.select
import com.faltenreich.diaguard.resource.start
import com.faltenreich.diaguard.resource.store
import com.faltenreich.diaguard.view.layout.WizardStepListItem
import com.faltenreich.diaguard.view.layout.WizardStepState
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReadBackupForm(
    state: ReadBackupFormState?,
    onIntent: (ReadBackupFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

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
            Button(onClick = { onIntent(ReadBackupFormIntent.Select) }) {
                Text(stringResource(Res.string.select))
            }
        }

        WizardStepListItem(
            index = 1,
            label = stringResource(Res.string.backup_read_start),
            state = WizardStepState.UPCOMING,
        ) {
            Button(onClick = { onIntent(ReadBackupFormIntent.Read) }) {
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
            Button(onClick = { onIntent(ReadBackupFormIntent.Check) }) {
                Text(stringResource(Res.string.start))
            }
        }

        WizardStepListItem(
            index = 4,
            label = stringResource(Res.string.backup_read_confirm),
            state = WizardStepState.UPCOMING,
        ) {
            Button(onClick = { onIntent(ReadBackupFormIntent.Store) }) {
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

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    ReadBackupForm(
        // TODO: Add missing states when PreviewParameter is working
        state = ReadBackupFormState.Idle,
        onIntent = {},
    )
}