package com.faltenreich.diaguard.log

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_picker_open
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_date
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource

@Serializable
data object LogScreen : Screen {

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<LogViewModel>()
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = Res.string.search_open,
                    onClick = { viewModel.dispatchIntent(LogIntent.OpenEntrySearch()) },
                )
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_date),
                    contentDescription = Res.string.date_picker_open,
                    onClick = { viewModel.dispatchIntent(LogIntent.OpenDatePickerDialog) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(LogIntent.CreateEntry()) },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.entry_new_description),
                    )
                }
            },
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<LogViewModel>()
        Log(
            state = viewModel.collectState(),
            items = viewModel.pagingData.collectAsLazyPagingItems(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}