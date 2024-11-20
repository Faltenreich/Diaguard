package com.faltenreich.diaguard.backup.user.write

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.ExtendedFloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_write
import diaguard.shared.generated.resources.retry
import diaguard.shared.generated.resources.start
import diaguard.shared.generated.resources.store
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object WriteBackupFormScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.backup_write))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<WriteBackupFormViewModel>()
        val state = viewModel.collectState() ?: return BottomAppBarStyle.Visible()
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
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
        )
    }

    @Composable
    override fun Content() {
        WriteBackupForm(viewModel = viewModel())
    }
}