package com.faltenreich.diaguard.export

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.export
import diaguard.shared.generated.resources.ic_check
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource

@Serializable
data object ExportFormScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.export))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<ExportFormViewModel>()
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.submit() }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.export),
                    )
                }
            }
        )
    }

    @Composable
    override fun Content() {
        ExportForm(viewModel = viewModel())
    }
}