package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.log.LogIntent
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_pick
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_date_range
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import org.jetbrains.compose.resources.painterResource

data object LogScreen : Screen {

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<LogViewModel>()
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = Res.string.search_open,
                    onClick = { viewModel.dispatchIntent(LogIntent.SearchEntries) },
                )
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_date_range),
                    contentDescription = Res.string.date_pick,
                    onClick = { viewModel.dispatchIntent(LogIntent.SelectDate) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<LogViewModel>()
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

    @Composable
    override fun Content() {
        Log(viewModel = getViewModel())
    }
}