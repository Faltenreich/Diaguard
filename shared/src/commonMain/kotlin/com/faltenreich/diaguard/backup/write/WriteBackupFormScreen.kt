package com.faltenreich.diaguard.backup.write

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_write
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object WriteBackupFormScreen : com.faltenreich.diaguard.navigation.screen.Screen {

    @Composable
    override fun TopAppBar(): com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle {
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.backup_write))
        }
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<WriteBackupFormViewModel>()
        WriteBackupForm(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}