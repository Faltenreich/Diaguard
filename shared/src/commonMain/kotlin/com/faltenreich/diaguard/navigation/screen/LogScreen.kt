package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.log.LogIntent
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DatePickerBottomAppBarItem
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import dev.icerock.moko.resources.compose.painterResource
import org.koin.core.parameter.parametersOf

data class LogScreen(val date: Date? = null) : Screen {

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<LogViewModel> { parametersOf(date) }
                val currentDate = viewModel.currentDate.collectAsState().value
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_search),
                    contentDescription = MR.strings.search_open,
                    onClick = { viewModel.dispatchIntent(LogIntent.SearchEntries) },
                )
                DatePickerBottomAppBarItem(
                    date = currentDate,
                    onDatePick = viewModel::setDate,
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<LogViewModel> { parametersOf(date) }
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(LogIntent.CreateEntry()) },
                ) {
                    Icon(
                        painter = painterResource(MR.images.ic_add),
                        contentDescription = getString(MR.strings.entry_new_description),
                    )
                }
            },
        )

    @Composable
    override fun Content() {
        Log(viewModel = getViewModel { parametersOf(date) })
    }
}