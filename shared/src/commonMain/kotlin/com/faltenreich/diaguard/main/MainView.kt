package com.faltenreich.diaguard.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import com.faltenreich.diaguard.backup.user.read.ReadBackupFormScreen
import com.faltenreich.diaguard.backup.user.write.WriteBackupFormScreen
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.export.form.ExportFormScreen
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.form.FoodFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.main.menu.MainMenuScreen
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListScreen
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bar.top.TopAppBar
import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.navigation.navigate
import com.faltenreich.diaguard.navigation.screen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.preference.color.ColorSchemeFormScreen
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesFormScreen
import com.faltenreich.diaguard.preference.food.FoodPreferenceScreen
import com.faltenreich.diaguard.preference.license.LicenseListScreen
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceScreen
import com.faltenreich.diaguard.preference.screen.StartScreenFormScreen
import com.faltenreich.diaguard.shared.di.LocalSharedViewModelStoreOwner
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.di.rememberViewModelStoreOwner
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.statistic.StatisticScreen
import com.faltenreich.diaguard.tag.detail.TagDetailScreen
import com.faltenreich.diaguard.tag.list.TagListScreen
import com.faltenreich.diaguard.timeline.TimelineScreen
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_arrow_back
import diaguard.shared.generated.resources.navigate_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = inject(),
) {
    val state = viewModel.collectState()
    if (state !is MainState.SubsequentStart) return

    val navController = rememberNavController()
    var bottomSheet by remember { mutableStateOf<Screen?>(null) }
    var modal by remember { mutableStateOf<Modal?>(null) }

    LaunchedEffect(Unit) {
        viewModel.collectNavigationEvents { event ->
            when (event) {
                is NavigationEvent.PushScreen -> navController.navigate(
                    screen = event.screen,
                    popHistory = event.popHistory,
                )
                is NavigationEvent.PopScreen -> navController.popBackStack()
                is NavigationEvent.OpenBottomSheet -> bottomSheet = event.bottomSheet
                is NavigationEvent.CloseBottomSheet -> bottomSheet = null
                is NavigationEvent.OpenModal -> modal = event.modal
                is NavigationEvent.CloseModal -> modal = null
            }
        }
    }

    val viewModelStoreOwner = rememberViewModelStoreOwner()

    // Avoid recomposing Scaffold on changing preference
    val startScreen = remember { state.startScreen }

    CompositionLocalProvider(LocalSharedViewModelStoreOwner provides viewModelStoreOwner) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    style = state.topAppBarStyle,
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { viewModel.dispatchIntent(MainIntent.PopScreen) }) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_arrow_back),
                                    contentDescription = getString(Res.string.navigate_back),
                                )
                            }
                        }
                    },
                )
            },
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
                    screen<FoodPreferenceScreen>()

                    screen<StatisticScreen>()
                    screen<ExportFormScreen>()

                    screen<OverviewPreferenceScreen>()
                    screen<ColorSchemeFormScreen>()
                    screen<StartScreenFormScreen>()
                    screen<DecimalPlacesFormScreen>()
                    screen<WriteBackupFormScreen>()
                    screen<ReadBackupFormScreen>()
                    screen<MeasurementCategoryListScreen>()
                    screen<MeasurementCategoryFormScreen>()
                    screen<MeasurementPropertyFormScreen>()
                    screen<TagListScreen>()
                    screen<TagDetailScreen>()
                    screen<LicenseListScreen>()
                }

                bottomSheet?.let { bottomSheet ->
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.dispatchIntent(MainIntent.CloseBottomSheet) },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    ) {
                        bottomSheet.Content()
                    }
                }

                modal?.Content()
            },
            bottomBar = {
                BottomAppBar(
                    style = state.bottomAppBarStyle,
                    onMenuClick = {
                        val currentDestination = navController.currentDestination?.route
                        viewModel.dispatchIntent(MainIntent.OpenBottomSheet(MainMenuScreen(currentDestination)))
                    },
                )
            },
        )
    }
}