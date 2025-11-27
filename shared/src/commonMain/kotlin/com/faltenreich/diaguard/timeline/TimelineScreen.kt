package com.faltenreich.diaguard.timeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.date_picker_open
import com.faltenreich.diaguard.resource.entry_new_description
import com.faltenreich.diaguard.resource.ic_add
import com.faltenreich.diaguard.resource.ic_date
import com.faltenreich.diaguard.resource.ic_search
import com.faltenreich.diaguard.resource.search_open
import com.faltenreich.diaguard.view.animation.rememberAnimatable
import com.faltenreich.diaguard.view.bar.BottomAppBarItem
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
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
                TooltipFloatingActionButton(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.entry_new_description),
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