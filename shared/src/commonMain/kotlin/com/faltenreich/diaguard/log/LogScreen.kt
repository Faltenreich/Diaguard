package com.faltenreich.diaguard.log

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_picker_open
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_date
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data object LogScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        val viewModel = viewModel<LogViewModel>()
        val state = viewModel.collectState()
        return TopAppBarStyle.CenterAligned {
            Text(state?.monthLocalized ?: "")
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<LogViewModel>()
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = stringResource(Res.string.search_open),
                    onClick = { viewModel.dispatchIntent(LogIntent.OpenEntrySearch()) },
                )
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_date),
                    contentDescription = stringResource(Res.string.date_picker_open),
                    onClick = { viewModel.dispatchIntent(LogIntent.OpenDatePickerDialog) },
                )
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(Res.drawable.ic_add),
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