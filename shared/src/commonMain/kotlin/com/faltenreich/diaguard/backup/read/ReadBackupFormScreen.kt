package com.faltenreich.diaguard.backup.read

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.localization.di.viewModel
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_read
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object ReadBackupFormScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.backup_read))
        }
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<ReadBackupFormViewModel>()
        ReadBackupForm(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}