package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.top.StatusBarStyle
import com.faltenreich.diaguard.navigation.scaffold.Scaffold
import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.theme.ThemeViewModel
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvas
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import org.jetbrains.compose.resources.painterResource

@Composable
fun Timeline(
    viewModel: TimelineViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    Scaffold(
        modifier = modifier,
        topBar = {
            val themeViewModel = viewModel<ThemeViewModel>()
            val colorScheme = themeViewModel.collectState()
            val isLightMode = colorScheme == ColorScheme.LIGHT || !isSystemInDarkTheme()
            val statusBarStyle = if (isLightMode) StatusBarStyle.Light else StatusBarStyle.Dark
            // TODO: return TopAppBarStyle.Hidden(statusBarStyle = statusBarStyle)
        },
        bottomBar = {
            BottomAppBar(
                onMenuClick = { viewModel.dispatchIntent(TimelineIntent.OpenMainMenu) },
                actions = {
                    BottomAppBarItem(
                        painter = painterResource(Res.drawable.ic_search),
                        contentDescription = Res.string.search_open,
                        onClick = { viewModel.dispatchIntent(TimelineIntent.SearchEntries) },
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { viewModel.dispatchIntent(TimelineIntent.CreateEntry) },
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add),
                            contentDescription = getString(Res.string.entry_new_description),
                        )
                    }
                },
            )
        },
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .background(AppTheme.colors.scheme.background),
            )
            TimelineCanvas(
                state = state,
                onIntent = viewModel::dispatchIntent,
                viewModel = viewModel,
                modifier = Modifier.weight(1f),
            )
            TimelineDateBar(
                label = state.currentDateLabel,
                onIntent = viewModel::dispatchIntent,
            )
        }
    }
}