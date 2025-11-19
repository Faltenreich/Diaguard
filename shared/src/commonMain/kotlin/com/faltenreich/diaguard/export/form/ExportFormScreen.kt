package com.faltenreich.diaguard.export.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.core.view.generated.resources.ic_check
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.export
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data object ExportFormScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.export))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<ExportFormViewModel>()
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
                FloatingActionButton(
                    painter = painterResource(diaguard.core.view.generated.resources.Res.drawable.ic_check),
                    contentDescription = stringResource(Res.string.export),
                    onClick = { viewModel.submit() },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<ExportFormViewModel>()
        ExportForm(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}