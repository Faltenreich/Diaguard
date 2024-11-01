package com.faltenreich.diaguard.main

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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.export.ExportFormScreen
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.form.FoodFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.main.menu.MainMenuScreen
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListScreen
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bar.snack.AndroidxSnackbarNavigation
import com.faltenreich.diaguard.navigation.bar.snack.SnackbarNavigation
import com.faltenreich.diaguard.navigation.bar.top.TopAppBar
import com.faltenreich.diaguard.navigation.screen
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesFormScreen
import com.faltenreich.diaguard.preference.food.FoodPreferenceScreen
import com.faltenreich.diaguard.preference.license.LicenseListScreen
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceScreen
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
    navigation: Navigation = inject(),
) {
    val state = viewModel.collectState()
    if (state !is MainState.SubsequentStart) return

    val navController = rememberNavController()
    // TODO: Get rid off side effects
    SideEffect {
        navigation.navController = navController
    }
    val snackbarHostState = remember { SnackbarHostState() }
    SideEffect {
        (inject<SnackbarNavigation>() as? AndroidxSnackbarNavigation)?.let {
            it.snackbarState = snackbarHostState
        }
    }

    LaunchedEffect(Unit) {
        viewModel.collectNavigationEvents { event ->
            when (event) {
                is NavigationEvent.PushScreen -> {
                    navController.navigate(
                        route = event.screen,
                        navOptions = NavOptions.Builder()
                            .run {
                                if (event.popHistory) setPopUpTo(
                                    route = navController.graph.findStartDestination().route,
                                    inclusive = true,
                                )
                                else this
                            }
                            .build()
                    )
                }
                is NavigationEvent.PopScreen -> {
                    event.result?.let { (key, value) ->
                        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle ?: return@let
                        savedStateHandle[key] = value
                    }
                    navController.popBackStack()
                }
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
                    screen<DecimalPlacesFormScreen>()
                    screen<MeasurementCategoryListScreen>()
                    screen<MeasurementCategoryFormScreen>()
                    screen<MeasurementPropertyFormScreen>()
                    screen<TagListScreen>()
                    screen<TagDetailScreen>()
                    screen<LicenseListScreen>()
                }

                state.bottomSheet?.let { bottomSheet ->
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.dispatchIntent(MainIntent.CloseBottomSheet) },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    ) {
                        bottomSheet.Content()
                    }
                }

                state.modal?.Content()
            },
            bottomBar = {
                BottomAppBar(
                    style = state.bottomAppBarStyle,
                    onMenuClick = {
                        viewModel.dispatchIntent(
                            MainIntent.OpenBottomSheet(
                                MainMenuScreen
                            )
                        )
                    },
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        )
    }
}