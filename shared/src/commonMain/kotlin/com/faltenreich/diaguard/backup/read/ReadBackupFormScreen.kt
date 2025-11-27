package com.faltenreich.diaguard.backup.read

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.data.navigation.TopAppBarStyle
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.backup_read
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