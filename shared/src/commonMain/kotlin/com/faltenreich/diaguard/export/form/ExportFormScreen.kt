package com.faltenreich.diaguard.export.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.data.navigation.BottomAppBarStyle
import com.faltenreich.diaguard.data.navigation.TopAppBarStyle
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.export
import com.faltenreich.diaguard.resource.ic_check
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
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
                TooltipFloatingActionButton(
                    painter = painterResource(Res.drawable.ic_check),
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