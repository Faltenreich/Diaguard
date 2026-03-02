package com.faltenreich.diaguard.export.history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.export_history
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object ExportHistoryScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.export_history))
        }
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<ExportHistoryViewModel>()
        ExportHistory(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}