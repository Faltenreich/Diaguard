package com.faltenreich.diaguard.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.faltenreich.diaguard.backup.read.ReadBackupFormScreen
import com.faltenreich.diaguard.backup.write.WriteBackupFormScreen
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.export.form.ExportFormScreen
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.form.FoodFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.injection.LocalSharedViewModelStoreOwner
import com.faltenreich.diaguard.injection.rememberViewModelStoreOwner
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.main.menu.MainMenu
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListScreen
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListScreen
import com.faltenreich.diaguard.navigation.navigate
import com.faltenreich.diaguard.navigation.screen
import com.faltenreich.diaguard.preference.color.isDark
import com.faltenreich.diaguard.preference.food.FoodPreferenceListScreen
import com.faltenreich.diaguard.preference.license.LicenseListScreen
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceListScreen
import com.faltenreich.diaguard.statistic.StatisticScreen
import com.faltenreich.diaguard.system.notification.Shortcut
import com.faltenreich.diaguard.tag.detail.TagDetailScreen
import com.faltenreich.diaguard.tag.list.TagListScreen
import com.faltenreich.diaguard.timeline.TimelineScreen
import diaguard.core.view.generated.resources.ic_arrow_back
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.navigate_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainView(
    shortcut: Shortcut?,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {
    val state = viewModel.collectState() ?: return

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.collectNavigationEvents { event ->
            when (event) {
                is com.faltenreich.diaguard.navigation.NavigationEvent.PushScreen -> navController.navigate(
                    screen = event.screen,
                    popHistory = event.popHistory,
                )
                is com.faltenreich.diaguard.navigation.NavigationEvent.PopScreen -> navController.popBackStack()
                is com.faltenreich.diaguard.navigation.NavigationEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    val isDarkMode = state.colorScheme.isDark()
    LaunchedEffect(state.topAppBarStyle) {
        val isAppearanceLightStatusBars = state.topAppBarStyle is com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.Hidden && !isDarkMode
        viewModel.dispatchIntent(MainIntent.TintStatusBars(isAppearanceLightStatusBars))
    }

    val viewModelStoreOwner = rememberViewModelStoreOwner()

    // Avoid recomposing Scaffold on changing preference
    val startScreen = remember { state.startScreen }

    LaunchedEffect(shortcut) {
        when (shortcut) {
            Shortcut.CREATE_ENTRY -> navController.navigate(
                screen = EntryFormScreen(),
                popHistory = true,
            )
            null -> Unit
        }
    }

    CompositionLocalProvider(LocalSharedViewModelStoreOwner provides viewModelStoreOwner) {
        Scaffold(
            modifier = modifier,
            topBar = {
                _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBar(
                    style = state.topAppBarStyle,
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { viewModel.dispatchIntent(MainIntent.PopScreen) }) {
                                Icon(
                                    painter = painterResource(
                                        diaguard.core.view.generated.resources.Res.drawable.ic_arrow_back,
                                    ),
                                    contentDescription = stringResource(Res.string.navigate_back),
                                )
                            }
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            // Support edge-to-edge content, e.g. in Log
            contentWindowInsets = WindowInsets(top = 0),
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = startScreen,
                    modifier = Modifier.padding(padding),
                ) {
                    screen<DashboardScreen>()
                    screen<TimelineScreen>()
                    screen<LogScreen>()

                    screen<EntryFormScreen>()
                    screen<EntrySearchScreen>()
                    screen<FoodFormScreen>()
                    screen<FoodSearchScreen>()
                    screen<FoodEatenListScreen>()
                    screen<FoodPreferenceListScreen>()

                    screen<StatisticScreen>()
                    screen<ExportFormScreen>()

                    screen<OverviewPreferenceListScreen>()
                    screen<WriteBackupFormScreen>()
                    screen<ReadBackupFormScreen>()
                    screen<MeasurementCategoryListScreen>()
                    screen<MeasurementCategoryFormScreen>()
                    screen<MeasurementPropertyFormScreen>()
                    screen<MeasurementUnitListScreen>()
                    screen<TagListScreen>()
                    screen<TagDetailScreen>()
                    screen<LicenseListScreen>()
                }
            },
            bottomBar = {
                _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBar(
                    style = state.bottomAppBarStyle,
                    onMenuClick = { showMenu = true },
                )
            },
        )
    }

    if (showMenu) {
        ModalBottomSheet(
            onDismissRequest = { showMenu = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            MainMenu(
                currentDestination = navController.currentDestination?.route,
                onItemClick = { screen, popHistory ->
                    showMenu = false
                    viewModel.dispatchIntent(MainIntent.PushScreen(screen, popHistory))
                },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MainView(shortcut = null)
}