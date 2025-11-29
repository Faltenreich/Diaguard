package com.faltenreich.diaguard.timeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.shared.view.rememberAnimatable
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.date_picker_open
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_date
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data object TimelineScreen : Screen {

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<TimelineViewModel>()
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = stringResource(Res.string.search_open),
                    onClick = { viewModel.dispatchIntent(TimelineIntent.OpenEntrySearch()) },
                )
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_date),
                    contentDescription = stringResource(Res.string.date_picker_open),
                    onClick = { viewModel.dispatchIntent(TimelineIntent.OpenDatePickerDialog) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = getString(Res.string.entry_new_description),
                    onClick = { viewModel.dispatchIntent(TimelineIntent.CreateEntry) },
                )
            },
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<TimelineViewModel>()

        val scope = rememberCoroutineScope()
        val scrollOffset = rememberAnimatable()
        LaunchedEffect(Unit) {
            viewModel.collectEvents { event ->
                when (event) {
                    is TimelineEvent.Scroll -> scope.launch { scrollOffset.animateTo(event.offset) }
                }
            }
        }

        Timeline(
            state = viewModel.collectState(),
            scrollOffset = scrollOffset,
            onIntent = viewModel::dispatchIntent,
        )
    }
}