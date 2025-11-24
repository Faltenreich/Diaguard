package com.faltenreich.diaguard.log

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import diaguard.core.view.generated.resources.ic_add
import diaguard.feature.navigation.generated.resources.ic_search
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_picker_open
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_date
import diaguard.shared.generated.resources.search_open
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data object LogScreen : com.faltenreich.diaguard.navigation.screen.Screen {

    @Composable
    override fun TopAppBar(): com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle {
        val viewModel = viewModel<LogViewModel>()
        val state = viewModel.collectState()
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.CenterAligned {
            Text(state?.monthLocalized ?: "")
        }
    }

    @Composable
    override fun BottomAppBar(): com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle {
        val viewModel = viewModel<LogViewModel>()
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle.Visible(
            actions = {
                _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem(
                    painter = painterResource(diaguard.feature.navigation.generated.resources.Res.drawable.ic_search),
                    contentDescription = stringResource(Res.string.search_open),
                    onClick = { viewModel.dispatchIntent(LogIntent.OpenEntrySearch()) },
                )
                _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_date),
                    contentDescription = stringResource(Res.string.date_picker_open),
                    onClick = { viewModel.dispatchIntent(LogIntent.OpenDatePickerDialog) },
                )
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(diaguard.core.view.generated.resources.Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.entry_new_description),
                    onClick = { viewModel.dispatchIntent(LogIntent.CreateEntry()) },
                )
            },
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<LogViewModel>()
        Log(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}